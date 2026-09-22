package com.coursecart.catalog.service;

import com.coursecart.catalog.dto.*;
import com.coursecart.catalog.entity.Category;
import com.coursecart.catalog.entity.Course;
import com.coursecart.catalog.entity.CourseStatus;
import com.coursecart.catalog.entity.Lesson;
import com.coursecart.catalog.exception.ResourceNotFoundException;
import com.coursecart.catalog.repository.CategoryRepository;
import com.coursecart.catalog.repository.CourseRepository;
import com.coursecart.catalog.repository.LessonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogServiceImpl implements CatalogService {

    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    public CatalogServiceImpl(CategoryRepository categoryRepository, CourseRepository courseRepository, LessonRepository lessonRepository) {
        this.categoryRepository = categoryRepository;
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
    }

    // --- CATEGORIES ---

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryRequest request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Category name already exists");
        }
        Category category = new Category(request.getName());
        Category saved = categoryRepository.save(category);
        return mapToCategoryDTO(saved);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));

        if (!category.getName().equals(request.getName()) && categoryRepository.findByName(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Category name already exists");
        }

        category.setName(request.getName());
        Category updated = categoryRepository.save(category);
        return mapToCategoryDTO(updated);
    }

    @Override
    @Transactional
        public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
        List<Course> attachedCourses = courseRepository.findByCategoryId(id);
        
        boolean hasActiveCourses = attachedCourses.stream()
                .anyMatch(course -> course.getStatus() == CourseStatus.ACTIVE);
                
        if (hasActiveCourses) {
            throw new IllegalArgumentException("Cannot delete category because it has active courses attached.");
        }
        categoryRepository.delete(category);
    }

    // --- COURSES PUBLIC ---

    @Override
    public Page<CourseDTO> getActiveCourses(Pageable pageable, Long categoryId, String title) {
        Page<Course> courses;
        if (categoryId != null && title != null && !title.isEmpty()) {
            courses = courseRepository.findByStatusAndCategoryIdAndTitleContainingIgnoreCase(CourseStatus.ACTIVE, categoryId, title, pageable);
        } else if (categoryId != null) {
            courses = courseRepository.findByStatusAndCategoryId(CourseStatus.ACTIVE, categoryId, pageable);
        } else if (title != null && !title.isEmpty()) {
            courses = courseRepository.findByStatusAndTitleContainingIgnoreCase(CourseStatus.ACTIVE, title, pageable);
        } else {
            courses = courseRepository.findByStatus(CourseStatus.ACTIVE, pageable);
        }
        return courses.map(this::mapToCourseDTO);
    }

    @Override
    public CourseDetailDTO getActiveCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));

        return mapToCourseDetailDTO(course);
    }

    // --- COURSES ADMIN ---

    @Override
    public List<CourseDTO> getAllCoursesAdmin() {
        return courseRepository.findAll().stream()
                .map(this::mapToCourseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CourseDTO createCourse(CourseRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Course course = new Course(category, request.getTitle(), request.getDescription(), request.getPrice(), CourseStatus.DRAFT);
        course.setOriginalPrice(request.getOriginalPrice());
        course.setInstructorName(request.getInstructorName());
        Course saved = courseRepository.save(course);
        return mapToCourseDTO(saved);
    }

    @Override
    @Transactional
    public CourseDTO updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        course.setCategory(category);
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setOriginalPrice(request.getOriginalPrice());
        course.setInstructorName(request.getInstructorName());

        Course updated = courseRepository.save(course);
        return mapToCourseDTO(updated);
    }

    @Override
    @Transactional
    public CourseDTO updateCourseStatus(Long id, CourseStatusRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
                
        if (request.getStatus() == CourseStatus.ACTIVE && course.getCategory() == null) {
            throw new IllegalArgumentException("Cannot activate a course without a category.");
        }
        
        course.setStatus(request.getStatus());
        Course updated = courseRepository.save(course);
        return mapToCourseDTO(updated);
    }

    @Override
    @Transactional
        public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByDisplayOrderAsc(id);
        for (Lesson l : lessons) {
            lessonRepository.delete(l);
        }
        courseRepository.delete(course);
    }

    @Override
    public long countActiveCourses() {
        return courseRepository.countByStatus(CourseStatus.ACTIVE);
    }

    // --- LESSONS ---

    @Override
    public List<LessonDTO> getLessonsForCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return lessonRepository.findByCourseIdOrderByDisplayOrderAsc(course.getId()).stream()
                .map(this::mapToLessonDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LessonDTO createLesson(Long courseId, LessonRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Lesson lesson = new Lesson(course, request.getTitle(), request.getContent(), request.getDisplayOrder());
        Lesson saved = lessonRepository.save(lesson);
        return mapToLessonDTO(saved);
    }

    @Override
    @Transactional
    public LessonDTO updateLesson(Long courseId, Long lessonId, LessonRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        if (!lesson.getCourse().getId().equals(course.getId())) {
            throw new IllegalArgumentException("Lesson does not belong to the specified course");
        }

        lesson.setTitle(request.getTitle());
        lesson.setContent(request.getContent());
        lesson.setDisplayOrder(request.getDisplayOrder());

        Lesson updated = lessonRepository.save(lesson);
        return mapToLessonDTO(updated);
    }

    @Override
    @Transactional
    public void deleteLesson(Long courseId, Long lessonId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        if (!lesson.getCourse().getId().equals(course.getId())) {
            throw new IllegalArgumentException("Lesson does not belong to the specified course");
        }

        lessonRepository.delete(lesson);
    }

    // --- MAPPERS ---

    private CategoryDTO mapToCategoryDTO(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDTO(category.getId(), category.getName());
    }

    private CourseDTO mapToCourseDTO(Course course) {
        CourseDTO dto = new CourseDTO(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getPrice(),
                course.getStatus(),
                mapToCategoryDTO(course.getCategory()),
                course.getOriginalPrice(),
                course.getInstructorName(),
                course.getRating(),
                course.getRatingCount(),
                course.getBestseller(),
                course.getLessonCount()
        );
        dto.setImageUrl(course.getImageUrl());
        return dto;
    }

    private CourseDetailDTO mapToCourseDetailDTO(Course course) {
        CourseDetailDTO dto = new CourseDetailDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setPrice(course.getPrice());
        dto.setStatus(course.getStatus());
        dto.setImageUrl(course.getImageUrl());
        dto.setCategory(mapToCategoryDTO(course.getCategory()));
        
        List<LessonDTO> lessons = lessonRepository.findByCourseIdOrderByDisplayOrderAsc(course.getId())
                .stream()
                .map(this::mapToLessonDTO)
                .collect(Collectors.toList());
        dto.setLessons(lessons);
        
        return dto;
    }

    private LessonDTO mapToLessonDTO(Lesson lesson) {
        return new LessonDTO(
                lesson.getId(),
                lesson.getCourse().getId(),
                lesson.getTitle(),
                lesson.getContent(),
                lesson.getDisplayOrder()
        );
    }
}







