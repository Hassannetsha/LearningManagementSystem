package org.example.lmsproject.quiz.model.Quiz;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.quiz.model.Question.QuestionBank;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "quiz", uniqueConstraints = @UniqueConstraint(columnNames = {"quizName", "courseId"}))
public class QuizEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;
    @ManyToOne
    private Course course;
//    private Long courseId;
    @Column(nullable = false)
    private String quizName;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private QuestionBank questionBank;
    // public long getCourse() {
    //     return getCourseId();
    // }

    public String getQuizName() {
        return this.quizName;
    }

//    public void setCourseId(Long courseId) {
//        this. = courseId;
//    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    @Override
    public String toString() {
        return "Quiz{\"quizName\": \"" + quizName + "\", \"course id\": \"" + course.getCourseId() + "\"}";
    }

    public Long getQuizId() {
        return quizId;
    }

    public QuestionBank getQuestionBank() {
        return questionBank;
    }

    public void setQuestionBank(QuestionBank questionBank) {
        this.questionBank = questionBank;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
