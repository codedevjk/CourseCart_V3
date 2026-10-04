import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Course, CourseDetail, Category, Lesson, PageResponse } from '../models/catalog.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CatalogService {
  private apiUrl = `${environment.apiBaseUrl}/catalog`;

  constructor(private http: HttpClient) { }

  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.apiUrl}/categories`);
  }

  createCategory(category: Category): Observable<Category> {
    return this.http.post<Category>(`${this.apiUrl}/categories`, category);
  }

  updateCategory(id: number, category: Category): Observable<Category> {
    return this.http.put<Category>(`${this.apiUrl}/categories/${id}`, category);
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/categories/${id}`);
  }

  getCourses(categoryId?: number, search?: string, page?: number, size?: number): Observable<PageResponse<Course>> {
    // TODO[TRAINEE]: Implement getCourses
    return new Observable<PageResponse<Course>>();
  }

  getCourse(id: number): Observable<CourseDetail> {
    // TODO[TRAINEE]: Implement getCourse
    return new Observable<CourseDetail>();
  }

  getAdminCourses(): Observable<Course[]> {
    // TODO[TRAINEE]: Implement getAdminCourses
    return new Observable<Course[]>();
  }

  createCourse(course: Course): Observable<Course> {
    // TODO[TRAINEE]: Implement createCourse
    return new Observable<Course>();
  }

  updateCourse(id: number, course: Course): Observable<Course> {
    // TODO[TRAINEE]: Implement updateCourse
    return new Observable<Course>();
  }

  updateCourseStatus(id: number, status: string): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/courses/${id}/status`, { status });
  }

  getCourseCount(): Observable<{ totalCourses: number }> { // Contract didn't specify exact JSON key but implied count
    return this.http.get<{ totalCourses: number }>(`${this.apiUrl}/courses/count`);
  }

  getLessons(courseId: number): Observable<Lesson[]> {
    return this.http.get<Lesson[]>(`${this.apiUrl}/courses/${courseId}/lessons`);
  }

  createLesson(courseId: number, lesson: Lesson): Observable<Lesson> {
    return this.http.post<Lesson>(`${this.apiUrl}/courses/${courseId}/lessons`, lesson);
  }

  updateLesson(courseId: number, lessonId: number, lesson: Lesson): Observable<Lesson> {
    return this.http.put<Lesson>(`${this.apiUrl}/courses/${courseId}/lessons/${lessonId}`, lesson);
  }

  deleteLesson(courseId: number, lessonId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/courses/${courseId}/lessons/${lessonId}`);
  }
}
