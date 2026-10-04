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
    // TODO[TRAINEE]: Implement initialization logic
  }
}
