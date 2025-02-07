package org.example.lmsproject.quiz.model.Quiz;

import java.util.List;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.userPart.model.Student;
import org.example.lmsproject.userPart.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
// import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "quiz_submission")
public class QuizSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private QuizEntity quiz;
    @ManyToOne
    private Course course;
    @ManyToOne
    private Student student;
    @Column(nullable=false)
    private List<String> answers;
    public QuizSubmission(){
        
    }
    public QuizSubmission(QuizEntity quiz, Course course, List<String> answers,Student student) {
        this.quiz = quiz;
        this.course = course;
        this.answers = answers;
        this.student = student;
    }
    public Long getId() {
        return id;
    }
    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
    public QuizEntity getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizEntity quiz) {
        this.quiz = quiz;
    }
    @Override
    public String toString(){
        return course.getCourseId() + "\n" + answers + "\n" + quiz.toString() + "\n" + "\n";
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
