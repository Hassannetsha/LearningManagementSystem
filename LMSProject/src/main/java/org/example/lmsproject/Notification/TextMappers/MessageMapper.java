package org.example.lmsproject.Notification.TextMappers;


import org.example.lmsproject.assignment.model.Assignment;
import org.example.lmsproject.assignment.model.AssignmentSubmission;
import org.example.lmsproject.course.model.CourseEnrollRequest;
import org.example.lmsproject.quiz.model.Quiz.QuizEntity;
import org.example.lmsproject.userPart.model.Request;
import org.example.lmsproject.userPart.model.Response;

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
        }  else if (message instanceof CourseEnrollRequest) {
            if (((CourseEnrollRequest) message).getStatus().equalsIgnoreCase("Pending")) {
                return String.format("%s has requested to enroll in the course: %s.",
                        ((CourseEnrollRequest) message).getStudent().getUsername(),
                        ((CourseEnrollRequest) message).getCourse().getTitle());
            } else if (((CourseEnrollRequest) message).getStatus().equalsIgnoreCase("Accepted")) {
                return String.format("You have been accepted into the course '%s'. Congratulations!",
                        ((CourseEnrollRequest) message).getCourse().getTitle());
            } else {
                return String.format("Your enrollment request for the course '%s' has been declined.",
                        ((CourseEnrollRequest) message).getCourse().getTitle());
            }
        }  else if (message instanceof Request) {
            return String.format("There is a new request:\nUsername: %s\nEmail: %s\nRole Requested: %s.",
                    ((Request) message).getUsername(),
                    ((Request) message).getEmail(),
                    ((Request) message).getRole());
        } else if (message instanceof Response) {
            return "Welcome to the LMS! You are now a part of the system.";
        }
        return "";
    }
}

