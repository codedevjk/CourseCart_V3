import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${environment.apiBaseUrl}/users`;

  constructor(private http: HttpClient) { }

  register(user: Partial<User>): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/register`, user);
  }

  login(credentials: { username: string; password?: string }): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/login`, credentials);
  }

  getProfile(userId: number): Observable<User> {
    // TODO[TRAINEE]: Implement getProfile
    return new Observable<User>();
  }

  getUserCount(): Observable<{ totalUsers: number }> {
    return this.http.get<{ totalUsers: number }>(`${this.apiUrl}/count`);
  }
}
