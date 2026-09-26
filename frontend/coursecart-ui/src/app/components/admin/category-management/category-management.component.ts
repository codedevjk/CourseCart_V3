import { Component, OnInit } from '@angular/core';
import { CatalogService } from '../../../services/catalog.service';
import { Category } from '../../../models/catalog.model';

@Component({
  selector: 'app-category-management',
  templateUrl: './category-management.component.html',
  styleUrls: ['./category-management.component.css']
})
export class CategoryManagementComponent implements OnInit {

  categories: Category[] = [];
  isLoading = false;
  error = '';
  
  showForm = false;
  isEditing = false;
  showConfirmModal = false;
  showAlertModal = false;
  alertMessage = '';
  confirmMessage = '';
  confirmAction: (() => void) | null = null;
  currentCategory: Category = { id: 0, name: '' };
  formError = '';

  constructor(private catalogService: CatalogService) { }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.isLoading = true;
    this.error = '';
    this.catalogService.getCategories().subscribe({
      next: (data) => {
        this.categories = data;
        this.isLoading = false;
      },
      error: (err) => {

        this.error = 'Failed to load categories.';
        this.isLoading = false;
      }
    });
  }

  openCreateModal(): void {
    this.isEditing = false;
    this.currentCategory = { id: 0, name: '' };
    this.formError = '';
    this.showForm = true;
  }

  openEditModal(category: Category): void {
    this.isEditing = true;
    this.currentCategory = { ...category };
    this.formError = '';
    this.showForm = true;
  }

  cancelEdit(): void {
    this.currentCategory = { id: 0, name: '' };
    this.isEditing = false;
    this.showForm = false;
    this.formError = '';
  }

  saveCategory(): void {
    if (!this.currentCategory.name.trim()) {
      this.formError = 'Category name is required';
      return;
    }

    if (this.isEditing) {
      this.catalogService.updateCategory(this.currentCategory.id, this.currentCategory).subscribe({
        next: () => {
          this.loadCategories();
          this.cancelEdit();
        },
        error: (err) => {
          this.formError = err.error?.message || 'Failed to update category';
        }
      });
    } else {
      this.catalogService.createCategory(this.currentCategory).subscribe({
        next: () => {
          this.loadCategories();
          this.cancelEdit();
        },
        error: (err) => {
          this.formError = err.error?.message || 'Failed to create category';
        }
      });
    }
  }

  deleteCategory(id: number): void {
    this.confirmMessage = 'Are you sure you want to delete this category?';
    this.confirmAction = () => {
      this.showConfirmModal = false;
      this.catalogService.deleteCategory(id).subscribe({
        next: () => {
          this.loadCategories();
        },
        error: (err) => {
          this.alertMessage = err.error?.message || 'Failed to delete category';
          this.showAlertModal = true;
        }
      });
    };
    this.showConfirmModal = true;
  }

  executeConfirm() {
    if (this.confirmAction) {
      this.confirmAction();
      this.confirmAction = null;
    }
  }
}


