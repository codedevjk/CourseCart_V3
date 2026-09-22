package com.coursecart.catalog.dto;

import com.coursecart.catalog.entity.CourseStatus;
import java.math.BigDecimal;

public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private CourseStatus status;
    private CategoryDTO category;

    private BigDecimal originalPrice;
    private String instructorName;
    private Double rating;
    private Integer ratingCount;
    private Boolean bestseller;
    private String imageUrl;
    private Integer lessonCount;

    public CourseDTO() {
    }

    public CourseDTO(Long id, String title, String description, BigDecimal price, CourseStatus status, CategoryDTO category,
                     BigDecimal originalPrice, String instructorName, Double rating, Integer ratingCount, Boolean bestseller, Integer lessonCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.status = status;
        this.category = category;
        this.originalPrice = originalPrice;
        this.instructorName = instructorName;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.bestseller = bestseller;
        this.lessonCount = lessonCount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public CourseStatus getStatus() { return status; }
    public void setStatus(CourseStatus status) { this.status = status; }

    public CategoryDTO getCategory() { return category; }
    public void setCategory(CategoryDTO category) { this.category = category; }

    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Integer getRatingCount() { return ratingCount; }
    public void setRatingCount(Integer ratingCount) { this.ratingCount = ratingCount; }

    public Boolean getBestseller() { return bestseller; }
    public void setBestseller(Boolean bestseller) { this.bestseller = bestseller; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getLessonCount() { return lessonCount; }
    public void setLessonCount(Integer lessonCount) { this.lessonCount = lessonCount; }
}
