package project_software.lms.quiz.Entities.Question;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class TrueOrFalseQuestionEntity extends QuestionEntity{
    // @Column(nullable=false)
    // private boolean answers;
    @Column(nullable=false)
    private Boolean rightAnswer;

    // public boolean getAnswers() {
    //     return answers;
    // }

    // public void setAnswers(boolean answers) {
    //     this.answers = answers;
    // }

    public Boolean getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(Boolean rightAnswers) {
        this.rightAnswer = rightAnswers;
    }
    @Override
    public int calculateScore(Object userAnswer) {
        if(userAnswer instanceof String string){
            if (string.equalsIgnoreCase("true")) {
                return string.equalsIgnoreCase("true")? 1 : 0; 
            }
            else{
                return ((String) userAnswer).equalsIgnoreCase("false")? 1 : 0; 
            }
        }
        // if (userAnswer instanceof Boolean boolean1) {
            // return Objects.equals(rightAnswer, boolean1)? 1 : 0;
        // }
        return 0;
    }
}
