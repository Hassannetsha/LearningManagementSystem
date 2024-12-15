package project_software.main.quiz.Entities.Question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import project_software.main.quiz.Entities.QuizEntity;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(
        name = "question",
        uniqueConstraints = @UniqueConstraint(columnNames = {"question"})
)
public abstract class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String question;
    @Column(nullable = false)
    private String type;
    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private QuizEntity quiz;
    // @ManyToOne
    // @JoinColumn(name = "question_bank_id", nullable = false)
    // private QuestionBank questionBank;
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public QuizEntity getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizEntity quiz) {
        this.quiz = quiz;
    }

    // public QuestionBank getQuestionBank() {
    //     return questionBank;
    // }

    // public void setQuestionBank(QuestionBank questionBank) {
    //     this.questionBank = questionBank;
    // }

}
