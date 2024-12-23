package org.example.lmsproject.course.model;

import org.example.lmsproject.Notification.TextMappers.NotificationAndEmailMapper;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public class CourseEnrollRequestNotification implements NotificationAndEmailMapper{
    private final CourseEnrollRequest courseEnrollRequest;

    @Override
    public String getSubject() {
        if (courseEnrollRequest.getStatus().equalsIgnoreCase("Pending")) {
            return String.format("Enrollment request for %s",
                    courseEnrollRequest.getCourse().getTitle());
        } else if (courseEnrollRequest.getStatus().equalsIgnoreCase("Accepted")) {
            return String.format("Enrollment Accepted for %s!",
                    courseEnrollRequest.getCourse().getTitle());
        } else {
            return String.format("Enrollment Declined for %s",
                    courseEnrollRequest.getCourse().getTitle());
        }
    }

    @Override
    public String getBody() {
        if (courseEnrollRequest.getStatus().equalsIgnoreCase("Pending")) {
            return String.format(
                    "%s has requested to enroll in the course: %s. \n Please review and take the necessary action.",
                    courseEnrollRequest.getStudent().getUsername(),
                    courseEnrollRequest.getCourse().getTitle());
        } else if (courseEnrollRequest.getStatus().equalsIgnoreCase("Accepted")) {
            return String.format("You have been accepted into the course '%s'.\nCongratulations!",
                    courseEnrollRequest.getCourse().getTitle());
        } else {
            return String.format(
                    "Your enrollment request for the course '%s' has been declined.\nWe regret to inform you of this decision.",
                    courseEnrollRequest.getCourse().getTitle());
        }
    }
    
}
