package org.example.lmsproject.quiz.model.Question;

import java.util.List;

import org.example.lmsproject.course.model.Course;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
// import lombok.AllArgsConstructor;
// import lombok.NoArgsConstructor;

@Entity
// @AllArgsConstructor
// @NoArgsConstructor
public class MCQQuestionEntity extends QuestionEntity{
    @Column(nullable=false)
    private List<String>answers;
    @Column(nullable=false)
    private String rightAnswer;
    public MCQQuestionEntity(Long id, String question, String type, Course course, List<String> answers,
            String rightAnswer) {
        super(id, question, type, course);
        this.answers = answers;
        this.rightAnswer = rightAnswer;
    }
    public MCQQuestionEntity(List<String> answers, String rightAnswer) {
        this.answers = answers;
        this.rightAnswer = rightAnswer;
    }
    public MCQQuestionEntity(){

    }
    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswers) {
        this.rightAnswer = rightAnswers;
    }
    @Override
    public int calculateScore(Object userAnswer) {
        if (userAnswer instanceof String string) {
            return rightAnswer.equalsIgnoreCase(string) ? 1 : 0;
        }
        return 0;
    }
}
