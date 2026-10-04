import { Component, OnInit } from '@angular/core';
import { CatalogService } from '../../../services/catalog.service';
import { Course, Category } from '../../../models/catalog.model';

@Component({
  selector: 'app-course-management',
  templateUrl: './course-management.component.html',
  styleUrls: ['./course-management.component.css']
})
export class CourseManagementComponent implements OnInit {

  courses: Course[] = [];
  categories: Category[] = [];
  
  isLoading = false;
  error = '';
  
  showForm = false;
  isEditing = false;
  currentCourse: any = { id: 0, categoryId: null, title: '', description: '', price: 0, status: 'DRAFT' };
  formError = '';
  
  showConfirmModal = false;
  confirmMessage = '';
  confirmAction: (() => void) | null = null;

  constructor(private catalogService: CatalogService) { }

  ngOnInit(): void {
    this.loadCategories();
    this.loadCourses();
  }

  loadCategories(): void {
    this.catalogService.getCategories().subscribe({
      next: (data) => this.categories = data,
      error: (err) => {}
    });
  }

  loadCourses(): void {
    // TODO[TRAINEE]: Implement loadCourses
    this.isLoading = false;
  }

  openCreateModal(): void {
    this.showForm = true;
    this.isEditing = false;
    this.currentCourse = { id: 0, categoryId: null, title: '', description: '', price: 0, status: 'DRAFT' };
    this.formError = '';
  }

  openEditModal(course: any): void {
    this.showForm = true;
    this.isEditing = true;
    this.currentCourse = { 
      id: course.id, 
      categoryId: course.category?.id || course.categoryId, 
      title: course.title, 
      description: course.description, 
      price: course.price, 
      status: course.status 
    };
    this.formError = '';
  }

  cancelEdit(): void {
    this.showForm = false;
    this.isEditing = false;
    this.formError = '';
  }

  saveCourse(): void {
    // TODO[TRAINEE]: Implement saveCourse (create and update)
  }

  changeStatus(course: Course, newStatus: string): void {
    if (newStatus === 'ACTIVE' && !course.category) {
      this.error = 'Cannot activate an uncategorized course. Please assign a category first.';
      return;
    }
    this.confirmMessage = `Change status of "${course.title}" to ${newStatus}?`;
    this.confirmAction = () => {
      this.showConfirmModal = false;
      this.catalogService.updateCourseStatus(course.id, newStatus).subscribe({
        next: () => this.loadCourses(),
        error: (err) => this.error = err.error?.message || 'Failed to change status'
      });
    };
    this.showConfirmModal = true;
  }

  getCategoryName(categoryId: number): string {
    const cat = this.categories.find(c => c.id === categoryId);
    return cat ? cat.name : 'Unknown';
  }

  executeConfirm() {
    if (this.confirmAction) {
      this.confirmAction();
      this.confirmAction = null;
    }
  }
}

