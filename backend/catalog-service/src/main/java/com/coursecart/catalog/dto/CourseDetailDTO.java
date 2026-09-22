package com.coursecart.catalog.dto;

import java.util.List;

public class CourseDetailDTO extends CourseDTO {
    private List<LessonDTO> lessons;

    public CourseDetailDTO() {
        super();
    }

    public List<LessonDTO> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonDTO> lessons) {
        this.lessons = lessons;
    }
}
