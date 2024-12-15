package project_software.lms.quiz.Entities.Question;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class MCQQuestionEntity extends QuestionEntity{
    @Column(nullable=false)
    private List<String>answers;
    @Column(nullable=false)
    private String rightAnswers;

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public String getRightAnswer() {
        return rightAnswers;
    }

    public void setRightAnswer(String rightAnswers) {
        this.rightAnswers = rightAnswers;
    }
}
