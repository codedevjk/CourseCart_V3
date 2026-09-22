package com.coursecart.catalog.dto;

public class LessonDTO {
    private Long id;
    private Long courseId;
    private String title;
    private String content;
    private Integer displayOrder;

    public LessonDTO() {
    }

    public LessonDTO(Long id, Long courseId, String title, String content, Integer displayOrder) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.content = content;
        this.displayOrder = displayOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
