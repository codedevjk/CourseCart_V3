import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { AuthService } from '../../../services/auth.service';
import { EnrollmentService } from '../../../services/enrollment.service';
import { User } from '../../../models/user.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  user: User | null = null;
  coursesOwned: number = 0;
  isLoading = true;
  errorMessage = '';

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private enrollmentService: EnrollmentService
  ) {}

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      this.errorMessage = 'You must be logged in to view your profile.';
      this.isLoading = false;
      return;
    }

    this.userService.getProfile(currentUser.id).subscribe({
      next: (data) => {
        this.user = data;
        
        if (this.user.role === 'USER') {
          this.enrollmentService.getEnrollments(this.user.id).subscribe({
            next: (enrollments) => {
              this.coursesOwned = enrollments.length;
              this.isLoading = false;
            },
            error: () => {
              // Gracefully handle enrollment fetch failure
              this.isLoading = false;
            }
          });
        } else {
          this.isLoading = false;
        }
      },
      error: () => {
        this.errorMessage = 'Failed to load profile. Please try again.';
        this.isLoading = false;
      }
    });
  }
}
