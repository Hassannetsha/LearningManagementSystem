package org.example.lmsproject.Notification.TextMappers;

import org.example.lmsproject.quiz.model.Quiz.QuizEntity;


public class NewQuizNotification implements NotificationAndEmailMapper {
    private final String quizName;
    private final String CourseTitle;

    public NewQuizNotification(QuizEntity quiz) {
        this.quizName = quiz.getQuizName();
        this.CourseTitle = quiz.getCourse().getTitle();
    }
    @Override
    public String getSubject(){
        return String.format("New Quiz Available: '%s'",
                    this.quizName);
    }
    @Override
    public String getBody(){
        return String.format(
            "A new quiz '%s' has been uploaded in the course '%s'.\nYou can now attempt it on the LMS.",
            this.quizName,
            this.CourseTitle);
    }
    
}
