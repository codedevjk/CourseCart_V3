package com.coursecart.commerce.dto;

import java.math.BigDecimal;

public class CourseDTO {
    private Long id;
    private String title;
    private BigDecimal price;
    private String status;

    public CourseDTO() {
    }

    public CourseDTO(Long id, String title, BigDecimal price, String status) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
