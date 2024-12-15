package project_software.main.quiz.Entities;

// import java.util.List;

// import com.lms.project_advanced_software.quiz.Entities.Question.QuestionEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "quiz",
    uniqueConstraints = @UniqueConstraint(columnNames = {"quizName", "courseId"})
)
public class QuizEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;
    @Column(nullable=false)
    private long courseId;
    @Column(nullable=false)
    private String quizName;
    // List<QuestionEntity> questions;
    public long getCourseId() {
        return courseId;
    }

    public String getQuizName() {
        return this.quizName;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    @Override
    public String toString() {
        return "Quiz{\"quizName\": \"" + quizName + "\", \"course id\": \"" + courseId + "\"}";
    }
}