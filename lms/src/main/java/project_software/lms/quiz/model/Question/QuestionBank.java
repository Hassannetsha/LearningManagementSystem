package project_software.lms.quiz.model.Question;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "question_bank", uniqueConstraints = @UniqueConstraint(columnNames = {"questionBankName", "courseId"}))
public class QuestionBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String questionBankName;
    @Column(nullable=false)
    private Long courseId;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionEntity> questions;
    public Long getId() {
        return id;
    }
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public List<QuestionEntity> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionEntity> questions) {
        this.questions = questions;
    }

    public String getQuestionBankName() {
        return questionBankName;
    }

    public void setQuestionBankName(String questionBankName) {
        this.questionBankName = questionBankName;
    }
    public void removeQuestion(QuestionEntity question) {
        this.questions.remove(question);
    }
}
