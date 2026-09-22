package com.coursecart.enrollment.dto;

import java.sql.Timestamp;

public class EnrollmentDTO {
    private Long id;
    private Long userId;
    private Long courseId;
    private Timestamp enrolledAt;

    public EnrollmentDTO() {}

    public EnrollmentDTO(Long id, Long userId, Long courseId, Timestamp enrolledAt) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.enrolledAt = enrolledAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Timestamp getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(Timestamp enrolledAt) {
        this.enrolledAt = enrolledAt;
    }
}
