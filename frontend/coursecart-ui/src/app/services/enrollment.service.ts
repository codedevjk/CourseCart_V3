import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Enrollment } from '../models/enrollment.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EnrollmentService {
  private apiUrl = `${environment.apiBaseUrl}/enrollments`;

  constructor(private http: HttpClient) { }

  getEnrollments(userId: number): Observable<Enrollment[]> {
    // TODO[TRAINEE]: Implement getEnrollments
    return new Observable<Enrollment[]>();
  }

  getLessonProgress(enrollmentId: number): Observable<number[]> {
    return this.http.get<number[]>(`${this.apiUrl}/${enrollmentId}/progress`);
  }

  completeLesson(enrollmentId: number, lessonId: number, completed: boolean = true): Observable<void> {
    // TODO[TRAINEE]: Implement completeLesson
    return new Observable<void>();
  }

  getEnrollmentCount(): Observable<{ totalEnrollments: number }> {
    return this.http.get<{ totalEnrollments: number }>(`${this.apiUrl}/count`);
  }
}
