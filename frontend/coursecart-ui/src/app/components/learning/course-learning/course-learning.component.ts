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
    // TODO[TRAINEE]: Implement loadLearningData
    this.isLoading = false;
  }

  selectLesson(lesson: Lesson): void {
    this.activeLesson = lesson;
  }

  isCompleted(lessonId: number): boolean {
    return this.completedLessonIds.has(lessonId);
  }

  toggleCompletion(event: any): void {
    // TODO[TRAINEE]: Implement toggleCompletion
  }

  get validCompletedCount(): number {
    if (!this.course || !this.course.lessons) return 0;
    let count = 0;
    for (const lesson of this.course.lessons) {
      if (this.completedLessonIds.has(lesson.id)) {
        count++;
      }
    }
    return count;
  }

  get progressPercentage(): number {
    if (!this.course || !this.course.lessons || this.course.lessons.length === 0) return 0;
    
    // Only count completed IDs that actually exist in the current course.lessons array
    // This safely handles cases where an admin deleted a lesson after a user completed it.
    return (this.validCompletedCount / this.course.lessons.length) * 100;
  }
}


