package org.example.lmsproject.Notification.TextMappers;

import java.util.List;

import org.example.lmsproject.quiz.model.Question.MCQQuestionEntity;
import org.example.lmsproject.quiz.model.Question.QuestionEntity;
import org.example.lmsproject.quiz.model.Question.TrueOrFalseQuestionEntity;
import org.example.lmsproject.quiz.model.Quiz.AutomatedFeedBack;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public class AutomatedFeedBackNotification implements NotificationAndEmailMapper{
    private final AutomatedFeedBack automatedFeedBack;
    @Override
    public String getSubject() {
        return String.format("Feedback for Quiz: %s",
                    automatedFeedBack.getQuiz().getQuizName());
    }

    @Override
    public String getBody() {
            List<QuestionEntity> questions = automatedFeedBack.getQuiz().getQuestionBank().getQuestions();
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
            return String.format("""
                                 Student ID: %d
                                 %sYour Score: %d out of %d""",
                    automatedFeedBack.getStudent().getId(),
                    feedbackDetails.toString(),
                    automatedFeedBack.getGrade(), automatedFeedBack.getTotalNumberOfQuestions());
    }
}
