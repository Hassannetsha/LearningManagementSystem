package org.example.lmsproject.quiz.model.Quiz;

import jakarta.persistence.*;
import org.example.lmsproject.course.model.Course;
// import jakarta.persistence.ManyToOne;
import org.example.lmsproject.quiz.model.Question.QuestionBank;

@Entity
@Table(name = "quiz", uniqueConstraints = @UniqueConstraint(columnNames = {"quizName", "courseId"}))
public class QuizEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;
    @Column(nullable = false)
    @ManyToOne
    private Course course;
//    private Long courseId;
    @Column(nullable = false)
    private String quizName;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private QuestionBank questionBank;
    public long getCourseId() {
        return getCourseId();
    }

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

    // public Course getCourse() {
    //     return course;
    // }

    // public void setCourse(Course course) {
    //     this.course = course;
    // }
}
