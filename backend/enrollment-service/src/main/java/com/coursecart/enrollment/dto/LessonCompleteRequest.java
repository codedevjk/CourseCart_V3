package com.coursecart.enrollment.dto;

import jakarta.validation.constraints.NotNull;

public class LessonCompleteRequest {

    @NotNull(message = "Completed status is required")
    private Boolean completed;

    public LessonCompleteRequest() {}

    public LessonCompleteRequest(Boolean completed) {
        this.completed = completed;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
