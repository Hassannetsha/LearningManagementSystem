package project_software.lms.quiz.Entities.Question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class TrueOrFalseQuestionEntity extends QuestionEntity{
    // @Column(nullable=false)
    // private boolean answers;
    @Column(nullable=false)
    private boolean rightAnswers;

    // public boolean getAnswers() {
    //     return answers;
    // }

    // public void setAnswers(boolean answers) {
    //     this.answers = answers;
    // }

    public boolean getRightAnswers() {
        return rightAnswers;
    }

    public void setRightAnswers(boolean rightAnswers) {
        this.rightAnswers = rightAnswers;
    }
}
