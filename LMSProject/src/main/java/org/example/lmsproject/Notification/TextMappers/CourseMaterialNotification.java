package org.example.lmsproject.Notification.TextMappers;

import org.example.lmsproject.course.model.CourseMaterial;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public class CourseMaterialNotification implements NotificationAndEmailMapper{
    private final CourseMaterial courseMaterial;

    @Override
    public String getSubject() {
        return String.format("New material has been uploaded for course: %s",
                    courseMaterial.getCourse().getTitle());
    }

    @Override
    public String getBody() {
        return String.format("A New file has been uploaded: %s \nplease check course  %s for details.",
                    courseMaterial.getFilename(), courseMaterial.getCourse().getTitle());
    }
    
}
