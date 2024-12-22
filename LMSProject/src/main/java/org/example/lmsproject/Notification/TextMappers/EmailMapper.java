package org.example.lmsproject.Notification.TextMappers;

import org.example.lmsproject.assignment.model.Assignment;
import org.example.lmsproject.assignment.model.AssignmentSubmission;
import org.example.lmsproject.course.model.CourseEnrollRequest;
import org.example.lmsproject.quiz.model.Question.MCQQuestionEntity;
import org.example.lmsproject.quiz.model.Question.QuestionEntity;
import org.example.lmsproject.quiz.model.Question.TrueOrFalseQuestionEntity;
import org.example.lmsproject.quiz.model.Quiz.FeedBack;
import org.example.lmsproject.quiz.model.Quiz.QuizEntity;
import org.example.lmsproject.userPart.model.Request;
import org.example.lmsproject.userPart.model.Response;

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
        } else if (message instanceof CourseEnrollRequest) {
            if (((CourseEnrollRequest) message).getStatus().equalsIgnoreCase("Pending")) {
                return String.format("Enrollment request for %s",
                        ((CourseEnrollRequest) message).getCourse().getTitle());
            } else if (((CourseEnrollRequest) message).getStatus().equalsIgnoreCase("Accepted")) {
                return String.format("Enrollment Accepted for %s!",
                        ((CourseEnrollRequest) message).getCourse().getTitle());
            } else {
                return String.format("Enrollment Declined for %s",
                        ((CourseEnrollRequest) message).getCourse().getTitle());
            }
        } else if (message instanceof Request) {
            return "New User Request Submitted";
        } else if (message instanceof Response) {
            return "User Authorization Success";
        } else if (message instanceof FeedBack) {
            return String.format("Feedback for Quiz: %s",
                    ((FeedBack) message).getQuiz().getQuizName());
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
        } else if (message instanceof CourseEnrollRequest) {
            if (((CourseEnrollRequest) message).getStatus().equalsIgnoreCase("Pending")) {
                return String.format("%s has requested to enroll in the course: %s. \n Please review and take the necessary action.",
                        ((CourseEnrollRequest) message).getStudent().getUsername(),
                        ((CourseEnrollRequest) message).getCourse().getTitle());
            } else if (((CourseEnrollRequest) message).getStatus().equalsIgnoreCase("Accepted")) {
                return String.format("You have been accepted into the course '%s'.\nCongratulations!",
                        ((CourseEnrollRequest) message).getCourse().getTitle());
            } else {
                return String.format("Your enrollment request for the course '%s' has been declined.\nWe regret to inform you of this decision.",
                        ((CourseEnrollRequest) message).getCourse().getTitle());
            }
        } else if (message instanceof Request) {
            return String.format("There is a new request:\nUsername: %s\nEmail: %s\nRole Requested: %s.",
                    ((Request) message).getUsername(),
                    ((Request) message).getEmail(),
                    ((Request) message).getRole());
        } else if (message instanceof Response) {
            return "Congratulations! You have been successfully authorized as a user in the LMS.";
        } else if (message instanceof FeedBack) {
            FeedBack feedback = (FeedBack) message;
            List<QuestionEntity> questions = feedback.getQuiz().getQuestionBank().getQuestions();

            StringBuilder feedbackDetails = new StringBuilder();
            for (QuestionEntity question : questions) {
                if (question instanceof MCQQuestionEntity mcq) {
                    feedbackDetails.append(String.format("%s\nAnswers: %s\nCorrect Answer: %s\n\n",
                            question.getQuestion(),
                            String.join(", ", mcq.getAnswers()),
                            mcq.getRightAnswer()));
                } else if (question instanceof TrueOrFalseQuestionEntity trueOrFalse) {
                    feedbackDetails.append(String.format("%s\nCorrect Answer: %s\n\n",
                            question.getQuestion(),
                            trueOrFalse.getRightAnswer() ? "True" : "False"));
                }
            }
            return String.format("Student ID: %d\n" +
                            "%sYour Score: %d",
                    feedback.getStudent().getId(),
                    feedbackDetails.toString(),
                    feedback.getGrade());
        }

        return "You have a new notification. Please check the LMS for more details.";
    }
}
