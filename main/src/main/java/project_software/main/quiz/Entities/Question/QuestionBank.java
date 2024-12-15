package project_software.main.quiz.Entities.Question;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import project_software.main.quiz.CompositePrimaryKeys.QuestionBankId;

@Entity
@IdClass(QuestionBankId.class)
@Table(name = "question_bank")
public class QuestionBank {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
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
}
