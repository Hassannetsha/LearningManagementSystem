package org.example.lmsproject.assignment.model;

import org.example.lmsproject.Notification.TextMappers.NotificationAndEmailMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AssignmentNotification implements NotificationAndEmailMapper{
    private final Assignment assignment;

    @Override
    public String getSubject() {
        return String.format("New Assignment Uploaded: '%s'",
                    (assignment).getTitle());
    }

    @Override
    public String getBody() {
        return String.format(
                    "A new assignment '%s' has been uploaded in the course '%s'.\nPlease check the LMS for details.",
                    (assignment).getTitle(),
                    (assignment).getCourse().getTitle());
    }

}
