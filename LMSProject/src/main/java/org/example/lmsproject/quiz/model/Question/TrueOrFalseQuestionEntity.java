package org.example.lmsproject.quiz.model.Question;

import org.example.lmsproject.course.model.Course;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class TrueOrFalseQuestionEntity extends QuestionEntity{
    @Column(nullable=false)
    private Boolean rightAnswer;

    public TrueOrFalseQuestionEntity(Long id, String question, String type, Course course, Boolean rightAnswer) {
        super(id, question, type, course);
        this.rightAnswer = rightAnswer;
    }

    public TrueOrFalseQuestionEntity() {
    }

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
                System.out.println(((String) userAnswer).equalsIgnoreCase("true"));
                return string.equalsIgnoreCase(rightAnswer.equals(1)?"true":"false")? 1 : 0; 
            }
            else{
                System.out.println(((String) userAnswer).equalsIgnoreCase("false"));
                return ((String) userAnswer).equalsIgnoreCase(rightAnswer.equals(1)?"true":"false")? 1 : 0; 
            }
        }

        return 0;
    }
}
