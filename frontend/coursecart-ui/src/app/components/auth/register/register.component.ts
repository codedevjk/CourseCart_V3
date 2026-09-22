import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'] // We will share login.component.css styles or duplicate them
})
export class RegisterComponent {
  user = {
    name: '',
    username: '',
    password: ''
  };
  errorMessage = '';
  isLoading = false;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit(): void {
    if (!this.user.name || !this.user.username || !this.user.password) {
      this.errorMessage = 'Please fill out all fields';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.userService.register(this.user).subscribe({
      next: (registeredUser) => {
        // According to US01, we might just get the user. Let's log them in automatically.
        // Actually, let's call login to be safe, or just set it if we trust it. 
        // US01 says "Success (201): { "id": 1, "name": "Jane", "username": "jane_doe", "role": "USER" }"
        // US02 says "Success (200): { "id": 1, ... } (Frontend saves this in localStorage)."
        // Let's set it directly.
        this.authService.setCurrentUser(registeredUser);
        this.router.navigate(['/courses']);
      },
      error: (err) => {
        this.isLoading = false;
        if (err.status === 409) {
          this.errorMessage = 'Username already exists. Please choose another one.';
        } else {
          this.errorMessage = 'An error occurred during registration. Please try again.';
        }
      }
    });
  }
}
