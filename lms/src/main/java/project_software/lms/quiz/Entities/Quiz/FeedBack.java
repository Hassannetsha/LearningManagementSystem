package project_software.lms.quiz.Entities.Quiz;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "feedback", uniqueConstraints = @UniqueConstraint(columnNames = {"quizId", "studentId"}))
public class FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "quizId", referencedColumnName = "quizId", nullable = false)
    private QuizEntity quiz;
    @Column(nullable = false)
    private Long studentId;
    @Column(nullable = false)
    private List<String> answers;
    @Column(nullable = false)
    private int grade;
    public FeedBack(){

    }
    public FeedBack(QuizEntity quiz, Long id) {
        this.quiz = quiz;
        this.studentId = id;
        // this.message = message;
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

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long id) {
        this.studentId = id;
    }

    public Long getId() {
        return id;
    }
    @Override
    public String toString(){
        String ans = "";
        for (String answer : this.answers) {
            ans += answer;
        }
        return quiz.toString() + "\n"  + studentId.toString() + "\n" + ans + "\n";
    }

    // public String getMessage() {
    //     return message;
    // }

    // public void setMessage(String message) {
    //     this.message = message;
    // }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
    
}
