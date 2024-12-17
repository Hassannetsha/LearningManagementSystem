package project_software.lms.quiz.Entities.Quiz;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import project_software.lms.quiz.Entities.Question.QuestionBank;

@Entity
@Table(name = "quiz", uniqueConstraints = @UniqueConstraint(columnNames = {"quizName", "courseId"}))
public class QuizEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;
    @Column(nullable = false)
    private Long courseId;
    @Column(nullable = false)
    private String quizName;
    @OneToOne
    private QuestionBank questionBank;
    public long getCourseId() {
        return courseId;
    }

    public String getQuizName() {
        return this.quizName;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    @Override
    public String toString() {
        return "Quiz{\"quizName\": \"" + quizName + "\", \"course id\": \"" + courseId + "\"}";
    }

    public Long getQuizId() {
        return quizId;
    }

    // public List<QuestionEntity> getQuestions() {
    //     return questions;
    // }

    // public void setQuestions(List<QuestionEntity> questions) {
    //     this.questions = questions;
    // }

    public QuestionBank getQuestionBank() {
        return questionBank;
    }

    public void setQuestionBank(QuestionBank questionBank) {
        this.questionBank = questionBank;
    }
}
