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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatalogServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private CatalogServiceImpl catalogService;

    private Category category;
    private Course course;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        category = new Category("Programming");
        category.setId(1L);

        course = new Course(category, "Java 101", "Learn Java", new BigDecimal("19.99"), CourseStatus.ACTIVE);
        course.setId(1L);

        lesson = new Lesson(course, "Intro to Java", "System.out.println", 1);
        lesson.setId(1L);
    }

    @Test
    void testCreateCategory() {
        CategoryRequest request = new CategoryRequest("Programming");
        when(categoryRepository.findByName(request.getName())).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDTO response = catalogService.createCategory(request);

        assertNotNull(response);
        assertEquals("Programming", response.getName());
    }

    @Test
    void testGetActiveCourses() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Course> coursePage = new PageImpl<>(Collections.singletonList(course));
        when(courseRepository.findByStatus(CourseStatus.ACTIVE, pageRequest)).thenReturn(coursePage);

        Page<CourseDTO> response = catalogService.getActiveCourses(pageRequest, null, null);

        assertEquals(1, response.getTotalElements());
        assertEquals("Java 101", response.getContent().get(0).getTitle());
    }

    @Test
    void testGetActiveCourseById_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.findByCourseIdOrderByDisplayOrderAsc(1L)).thenReturn(Collections.singletonList(lesson));

        CourseDetailDTO response = catalogService.getActiveCourseById(1L);

        assertNotNull(response);
        assertEquals("Java 101", response.getTitle());
        assertEquals(1, response.getLessons().size());
    }

    @Test
    void testGetActiveCourseById_DraftCourse_ThrowsException() {
        course.setStatus(CourseStatus.DRAFT);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        assertThrows(ResourceNotFoundException.class, () -> catalogService.getActiveCourseById(1L));
    }

    @Test
    void testCreateCourse() {
        CourseRequest request = new CourseRequest(1L, "Java 101", "Learn Java", new BigDecimal("19.99"));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        CourseDTO response = catalogService.createCourse(request);

        assertNotNull(response);
        assertEquals("Java 101", response.getTitle());
    }

    @Test
    void testUpdateCourseStatus() {
        CourseStatusRequest request = new CourseStatusRequest(CourseStatus.INACTIVE);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        
        Course updatedCourse = new Course(category, "Java 101", "Learn Java", new BigDecimal("19.99"), CourseStatus.INACTIVE);
        updatedCourse.setId(1L);
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);

        CourseDTO response = catalogService.updateCourseStatus(1L, request);

        assertEquals(CourseStatus.INACTIVE, response.getStatus());
    }

    // --- deleteCourse tests ---

    @Test
    void testDeleteCourse_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).delete(course);

        assertDoesNotThrow(() -> catalogService.deleteCourse(1L));

        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).delete(course);
    }

    @Test
    void testDeleteCourse_NotFound_ThrowsResourceNotFoundException() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> catalogService.deleteCourse(99L)
        );

        assertTrue(ex.getMessage().contains("99"));
        verify(courseRepository, never()).delete(any());
    }

    @Test
    void testDeleteCourse_WithDependentRecords_ThrowsDataIntegrityException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        // Simulate the JPA/DB raising a constraint violation when dependent FK records exist
        doThrow(new org.springframework.dao.DataIntegrityViolationException("FK constraint"))
                .when(courseRepository).delete(course);

        assertThrows(
                org.springframework.dao.DataIntegrityViolationException.class,
                () -> catalogService.deleteCourse(1L)
        );

        verify(courseRepository, times(1)).delete(course);
    }

    // --- deleteLesson tests ---

    @Test
    void testDeleteLesson_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        doNothing().when(lessonRepository).delete(lesson);

        assertDoesNotThrow(() -> catalogService.deleteLesson(1L, 1L));

        verify(lessonRepository, times(1)).delete(lesson);
    }

    @Test
    void testDeleteLesson_LessonNotBelongToCourse_ThrowsIllegalArgumentException() {
        // Create a different course that the lesson does NOT belong to
        Course otherCourse = new Course(category, "Python 101", "Learn Python", new BigDecimal("9.99"), CourseStatus.DRAFT);
        otherCourse.setId(2L);

        when(courseRepository.findById(2L)).thenReturn(Optional.of(otherCourse));
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson)); // lesson belongs to course id=1

        assertThrows(IllegalArgumentException.class, () -> catalogService.deleteLesson(2L, 1L));

        verify(lessonRepository, never()).delete(any());
    }

    // --- updateCourse tests ---

    @Test
    void testUpdateCourse_Success() {
        CourseRequest updateRequest = new CourseRequest(1L, "Java 201", "Advanced Java", new BigDecimal("29.99"));
        Course updatedCourse = new Course(category, "Java 201", "Advanced Java", new BigDecimal("29.99"), CourseStatus.ACTIVE);
        updatedCourse.setId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);

        CourseDTO response = catalogService.updateCourse(1L, updateRequest);

        assertNotNull(response);
        assertEquals("Java 201", response.getTitle());
        assertEquals(new BigDecimal("29.99"), response.getPrice());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    // --- createCategory duplicate tests ---

    @Test
    void testCreateCategory_DuplicateName_ThrowsIllegalArgumentException() {
        CategoryRequest request = new CategoryRequest("Programming");
        when(categoryRepository.findByName("Programming")).thenReturn(Optional.of(category));

        assertThrows(IllegalArgumentException.class, () -> catalogService.createCategory(request));

        verify(categoryRepository, never()).save(any());
    }

    // --- countActiveCourses tests ---

    @Test
    void testCountActiveCourses_ReturnsCount() {
        when(courseRepository.countByStatus(CourseStatus.ACTIVE)).thenReturn(5L);

        long count = catalogService.countActiveCourses();

        assertEquals(5L, count);
        verify(courseRepository, times(1)).countByStatus(CourseStatus.ACTIVE);
    }
}

