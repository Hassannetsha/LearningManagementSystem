package org.example.lmsproject.course.service;

import org.example.lmsproject.Notification.TextMappers.NotificationAndEmailMapper;
import org.example.lmsproject.course.model.Course;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public class CourseNotification implements NotificationAndEmailMapper {
    private final Course course;

    @Override
    public String getSubject() {
        return String.format("New update made for course: %s",
                    course.getTitle());
    }

    @Override
    public String getBody() {
        return String.format("New update made for course: %s",
                    course.getTitle());
    }
    
}
