package com.coursecart.catalog.dto;

import com.coursecart.catalog.entity.CourseStatus;
import jakarta.validation.constraints.NotNull;

public class CourseStatusRequest {
    
    @NotNull(message = "Status is required")
    private CourseStatus status;

    public CourseStatusRequest() {
    }

    public CourseStatusRequest(CourseStatus status) {
        this.status = status;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }
}
