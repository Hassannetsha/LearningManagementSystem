package org.example.lmsproject.Notification.TextMappers;


import org.example.lmsproject.assignment.model.Assignment;
import org.example.lmsproject.assignment.model.AssignmentSubmission;
import org.example.lmsproject.quiz.model.Quiz.QuizEntity;

public class MessageMapper {
    public static String toString(Object message) {
        if (message instanceof String) {
            return (String) message;
        } else if (message instanceof AssignmentSubmission) {
            return String.format("Your submission for Assignment '%s' in '%s' has been graded\n Feedback: %s",
                    ((AssignmentSubmission) message).getAssignment().getTitle(),
                    ((AssignmentSubmission) message).getAssignment().getCourse().getTitle(),
                    ((AssignmentSubmission) message).getFeedback());
        } else if (message instanceof Assignment) {
            return String.format("A new assignment '%s' has been uploaded in '%s'",
                    ((Assignment) message).getTitle(),
                    ((Assignment) message).getCourse().getTitle());
        } else if (message instanceof QuizEntity) {
            return String.format("A new Quiz '%s' has been uploaded in '%s'",
                    ((QuizEntity) message).getQuizName(),
                    ((QuizEntity) message).getCourse().getTitle());
        }
        return "";
    }
}

