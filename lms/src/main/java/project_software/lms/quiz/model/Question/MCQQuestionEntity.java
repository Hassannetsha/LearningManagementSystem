package project_software.lms.quiz.model.Question;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class MCQQuestionEntity extends QuestionEntity{
    @Column(nullable=false)
    private List<String>answers;
    @Column(nullable=false)
    private String rightAnswer;

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
