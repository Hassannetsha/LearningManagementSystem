package project_software.lms.quiz.DTOs.Quizzes;

// import java.util.List;

// import project_software.lms.quiz.Entities.Quiz.QuizEntity;

public class QuizSubmissionDTO {
    private Long courseId;
    private String answers;
    private Long quiz;
    private Long studentId;

    public long getCourseId() {
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

    public Long getQuiz() {
        return quiz;
    }

    public void setQuiz(Long quizId) {
        this.quiz = quizId;
    }
    @Override
    public String toString(){
        return courseId.toString() + "\n" + answers + "\n" + quiz.toString() + "\n" + "\n";
    }
}
