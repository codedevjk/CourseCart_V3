import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CatalogService } from '../../../services/catalog.service';
import { EnrollmentService } from '../../../services/enrollment.service';
import { CourseDetail } from '../../../models/catalog.model';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-course-detail',
  templateUrl: './course-detail.component.html',
  styleUrls: ['./course-detail.component.css']
})
export class CourseDetailComponent implements OnInit {
  courseId!: number;
  course: CourseDetail | null = null;
  
  isLoading = true;
  errorMessage = '';
  
  isLoggedIn = false;
  isAdmin = false;
  isEnrolled = false;

  constructor(
    private route: ActivatedRoute,
    private catalogService: CatalogService,
    private enrollmentService: EnrollmentService,
    public authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('courseId');
    if (idParam) {
      this.courseId = +idParam;
    } else {
      this.errorMessage = 'Invalid course ID';
      this.isLoading = false;
      return;
    }

    this.authService.currentUser$.subscribe(user => {
      this.isLoggedIn = !!user;
      this.isAdmin = this.authService.isAdmin();
      
      if (this.isLoggedIn && !this.isAdmin && user && this.courseId) {
        this.enrollmentService.getEnrollments(user.id).subscribe({
          next: (enrollments) => {
            this.isEnrolled = enrollments.some(e => e.courseId === this.courseId);
            this.fetchCourse();
          }
        });
      } else {
        this.fetchCourse();
      }
    });
  }

  fetchCourse(): void {
    this.catalogService.getCourse(this.courseId).subscribe({
      next: (data) => {
        this.course = data;
        
        // Enforce US 07: Inactive/Draft courses must disappear for new customers
        if (this.course.status !== 'ACTIVE' && !this.isEnrolled && !this.isAdmin) {
          this.errorMessage = 'This course is no longer available in the public catalog.';
          this.course = null;
        }
        
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load course details. It may be unavailable.';
        this.isLoading = false;
      }
    });
  }

  onEnrollClick(): void {
    if (!this.isLoggedIn) {
      this.router.navigate(['/login']);
    } else if (this.isEnrolled) {
      this.router.navigate(['/learning', this.courseId]);
    } else {
      this.router.navigate(['/checkout', this.courseId]);
    }
  }
}
