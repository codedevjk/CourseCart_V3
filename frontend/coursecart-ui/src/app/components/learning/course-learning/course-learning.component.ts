import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EnrollmentService } from '../../../services/enrollment.service';
import { CatalogService } from '../../../services/catalog.service';
import { AuthService } from '../../../services/auth.service';
import { CourseDetail, Lesson } from '../../../models/catalog.model';
import { Enrollment } from '../../../models/enrollment.model';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-course-learning',
  templateUrl: './course-learning.component.html',
  styleUrls: ['./course-learning.component.css']
})
export class CourseLearningComponent implements OnInit {
  courseId!: number;
  enrollmentId!: number;
  course: CourseDetail | null = null;
  completedLessonIds: Set<number> = new Set<number>();
  
  activeLesson: Lesson | null = null;
  isCourseAvailable = true;
  
  isLoading = true;
  errorMessage = '';
  actionError = '';

  constructor(
    private route: ActivatedRoute,
    private catalogService: CatalogService,
    private enrollmentService: EnrollmentService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('courseId');
    if (idParam) {
      this.courseId = +idParam;
      this.loadLearningData();
    } else {
      this.errorMessage = 'Invalid course ID';
      this.isLoading = false;
    }
  }

  loadLearningData(): void {
    const user = this.authService.getCurrentUser();
    if (!user) {
      this.router.navigate(['/login']);
      return;
    }

    this.enrollmentService.getEnrollments(user.id).subscribe({
      next: (enrollments) => {
        const enrollment = enrollments.find(e => e.courseId === this.courseId);
        if (!enrollment) {
          this.errorMessage = 'You are not enrolled in this course.';
          this.isLoading = false;
          return;
        }

        this.enrollmentId = enrollment.id;
        
        // Fetch course and progress
        forkJoin({
          course: this.catalogService.getCourse(this.courseId),
          progress: this.enrollmentService.getLessonProgress(this.enrollmentId).pipe(catchError(() => of([])))
        }).subscribe({
          next: (result) => {
            this.course = result.course;
            
            // Check if course is available
            if (!this.course.category) {
              this.isCourseAvailable = false;
            }
            this.completedLessonIds = new Set(result.progress);
            
            // Auto-select first lesson if available
            if (this.course.lessons && this.course.lessons.length > 0) {
              // Optionally select the first uncompleted lesson, or just the first one
              const firstUncompleted = this.course.lessons.find(l => !this.completedLessonIds.has(l.id));
              this.activeLesson = firstUncompleted || this.course.lessons[0];
            }
            
            this.isLoading = false;
          },
          error: (err) => {
            this.errorMessage = 'Failed to load course materials.';
            this.isLoading = false;
          }
        });
      },
      error: (err) => {
        this.errorMessage = 'Failed to verify enrollment.';
        this.isLoading = false;
      }
    });
  }

  selectLesson(lesson: Lesson): void {
    this.activeLesson = lesson;
  }

  isCompleted(lessonId: number): boolean {
    return this.completedLessonIds.has(lessonId);
  }

  markComplete(): void {
    if (!this.activeLesson || this.isCompleted(this.activeLesson.id)) return;
    
    const lessonId = this.activeLesson.id;
    this.enrollmentService.completeLesson(this.enrollmentId, lessonId).subscribe({
      next: () => {
        // Idempotent add
        this.completedLessonIds.add(lessonId);
      },
      error: (err) => {
        this.actionError = 'Failed to mark lesson as complete. Please try again.';
        setTimeout(() => this.actionError = '', 3000);
      }
    });
  }

  get progressPercentage(): number {
    if (!this.course || !this.course.lessons || this.course.lessons.length === 0) return 0;
    return (this.completedLessonIds.size / this.course.lessons.length) * 100;
  }
}


