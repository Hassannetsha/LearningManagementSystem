package org.example.lmsproject.Notification.TextMappers;

import org.example.lmsproject.assignment.model.Assignment;
import org.example.lmsproject.assignment.model.AssignmentSubmission;
import org.example.lmsproject.quiz.model.Quiz.QuizEntity;

import java.util.List;

public class EmailMapper {
    public static String getSubject(Object message) {
        if (message instanceof String) {
            return "Notification";
        } else if (message instanceof AssignmentSubmission) {
            return String.format("Graded Submission: '%s'",
                    ((AssignmentSubmission) message).getAssignment().getTitle());
        } else if (message instanceof Assignment) {
            return String.format("New Assignment Uploaded: '%s'",
                    ((Assignment) message).getTitle());
        } else if (message instanceof QuizEntity) {
            return String.format("New Quiz Available: '%s'",
                    ((QuizEntity) message).getQuizName());
        }
        return "LMS Notification";
    }
    public static String getBody(Object message) {
        if (message instanceof String) {
            return (String) message;
        } else if (message instanceof AssignmentSubmission) {
            return String.format("Your submission for the assignment '%s' in the course '%s' has been graded.\nFeedback: %s",
                    ((AssignmentSubmission) message).getAssignment().getTitle(),
                    ((AssignmentSubmission) message).getAssignment().getCourse().getTitle(),
                    ((AssignmentSubmission) message).getFeedback());
        } else if (message instanceof Assignment) {
            return String.format("A new assignment '%s' has been uploaded in the course '%s'.\nPlease check the LMS for details.",
                    ((Assignment) message).getTitle(),
                    ((Assignment) message).getCourse().getTitle());
        } else if (message instanceof QuizEntity) {
            return String.format("A new quiz '%s' has been uploaded in the course '%s'.\nYou can now attempt it on the LMS.",
                    ((QuizEntity) message).getQuizName(),
                    ((QuizEntity) message).getCourse().getTitle());
        }
        return "You have a new notification. Please check the LMS for more details.";
    }
}
