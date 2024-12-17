package project_software.lms.quiz.DTOs.Quizzes;

// import java.util.List;

public class QuizCreationDTO {
    private long courseId;
    private String quizName;
    private Long questionBankId;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public Long getQuestionBankId() {
        return questionBankId;
    }

    public void setQuestionBankId(Long questionIds) {
        this.questionBankId = questionIds;
    }
}
