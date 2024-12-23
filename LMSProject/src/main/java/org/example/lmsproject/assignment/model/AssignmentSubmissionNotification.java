package org.example.lmsproject.assignment.model;

import org.example.lmsproject.Notification.TextMappers.NotificationAndEmailMapper;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public class AssignmentSubmissionNotification implements NotificationAndEmailMapper {
    private final AssignmentSubmission assignmentSubmission;

    @Override
    public String getSubject() {
        return String.format("Graded Submission: '%s'",
                    (assignmentSubmission.getAssignment().getTitle()));
    }

    @Override
    public String getBody() {
        return String.format(
                    "Your submission for the assignment '%s' in the course '%s' has been graded.\nFeedback: %s",
                    (assignmentSubmission.getAssignment().getTitle()),
                    (assignmentSubmission.getAssignment().getCourse().getTitle()),
                    (assignmentSubmission.getFeedback()));
    }
    
}
