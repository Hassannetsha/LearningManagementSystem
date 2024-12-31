package org.example.lmsproject.quiz.model.Quiz;

// import java.util.ArrayList;
import java.util.List;

import org.example.lmsproject.quiz.model.Question.MCQQuestionEntity;
import org.example.lmsproject.quiz.model.Question.QuestionEntity;
import org.example.lmsproject.quiz.model.Question.TrueOrFalseQuestionEntity;
import org.example.lmsproject.userPart.model.Student;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
// import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "feedback")
public class AutomatedFeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "quizId", referencedColumnName = "quizId", nullable = false)
    private QuizEntity quiz;
    @ManyToOne
    private Student student;
    @Column(nullable = false)
    private List<String> answers;
    @Column(nullable = false)
    private int grade;
    public AutomatedFeedBack(){

    }
    public AutomatedFeedBack(QuizEntity quiz, Student student) {
        this.quiz = quiz;
        this.student = student;
    } 

    public QuizEntity getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizEntity quiz) {
        this.quiz = quiz;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
    public Long getId() {
        return id;
    }
    @Override
    public String toString(){
        String ans = "";
        String questions = "\n";
        int ctn = 1;
        for (QuestionEntity question : quiz.getQuestionBank().getQuestions()) {
            questions+= "Q" + ctn++ + " " + question.getQuestion() + "\nRight answer: "  ;
            if (question instanceof TrueOrFalseQuestionEntity trueOrFalseQuestionEntity) {
                questions+= trueOrFalseQuestionEntity.getRightAnswer().equals(1)?"True":"False" + '\n';
            }
            if (question instanceof MCQQuestionEntity mCQQuestionEntity) {
                questions+= mCQQuestionEntity.getRightAnswer() + '\n';
            }
            // questions+= "Q" + ctn++ + " ";
        }
        ctn = 1;
        for (String answer : this.answers) {
            ans += "\nQ"  + ctn++ + " " + answer;
        }
        return questions + "\n" + "Your answers: " + ans + "\n" + "Grade: " + this.grade + '\n';
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    public int getTotalNumberOfQuestions(){
        return quiz.getQuestionBank().getQuestions().size();
    }
    
}
