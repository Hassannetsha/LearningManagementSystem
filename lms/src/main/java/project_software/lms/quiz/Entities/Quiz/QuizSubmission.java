package project_software.lms.quiz.Entities.Quiz;

// import java.util.List;

// import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
// import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "quiz_submission", uniqueConstraints = @UniqueConstraint(columnNames = {"quiz", "courseId","StudentId"}))
public class QuizSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    // @JoinColumn(name = "quiz_id", referencedColumnName = "quizId", nullable = false)
    private QuizEntity quiz;
    @Column(nullable=false)
    private Long courseId;
    @Column(nullable=false)
    private Long studentId;
    @Column(nullable=false)
    private String answers;
    public QuizSubmission(){
        
    }
    public QuizSubmission(QuizEntity quiz, Long courseId, String answers,Long StudentId) {
        this.quiz = quiz;
        this.courseId = courseId;
        this.answers = answers;
        this.studentId = StudentId;
    }
    public Long getId() {
        return id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long StudentId) {
        this.studentId = StudentId;
    }

    public QuizEntity getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizEntity quiz) {
        this.quiz = quiz;
    }
    @Override
    public String toString(){
        return courseId.toString() + "\n" + answers + "\n" + quiz.toString() + "\n" + "\n";
    }
}
