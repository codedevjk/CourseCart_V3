import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CatalogService } from '../../../services/catalog.service';
import { Course, Category } from '../../../models/catalog.model';

@Component({
  selector: 'app-course-discovery',
  templateUrl: './course-discovery.component.html',
  styleUrls: ['./course-discovery.component.css']
})
export class CourseDiscoveryComponent implements OnInit {
  allCourses: Course[] = [];
  filteredCourses: Course[] = [];
  categories: Category[] = [];
  
  isLoading = true;
  errorMessage = '';

  searchQuery = '';
  selectedCategoryId: number | null = null;

  constructor(
    private catalogService: CatalogService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params['category']) {
        this.selectedCategoryId = Number(params['category']);
      }
    });
    this.fetchData();
  }

  fetchData(): void {
    this.isLoading = true;
    this.errorMessage = '';

    // Fetch categories and courses
    this.catalogService.getCategories().subscribe({
      next: (cats) => {
        this.categories = cats;
        // Fetch courses next (using a large size to grab all active ones for frontend filtering)
        this.catalogService.getCourses(undefined, undefined, 0, 100).subscribe({
          next: (page) => {
            this.allCourses = page.content;
            this.applyFilters();
            this.isLoading = false;
          },
          error: (err) => {
            this.errorMessage = 'Failed to load courses.';
            this.isLoading = false;
          }
        });
      },
      error: (err) => {
        this.errorMessage = 'Failed to load categories.';
        this.isLoading = false;
      }
    });
  }

  applyFilters(): void {
    this.filteredCourses = this.allCourses.filter(course => {
      const matchesSearch = !this.searchQuery || 
        course.title.toLowerCase().includes(this.searchQuery.toLowerCase()) || 
        course.description.toLowerCase().includes(this.searchQuery.toLowerCase());
        
      const matchesCategory = this.selectedCategoryId === null || 
        course.category?.id === this.selectedCategoryId;
        
      return matchesSearch && matchesCategory;
    });
  }

  selectCategory(categoryId: number | null): void {
    this.selectedCategoryId = categoryId;
    this.applyFilters();
  }
}

