package org.example.lmsproject.quiz.DTOs.Quizzes;

import java.util.List;

// import org.example.lmsproject.quiz.Entities.Quiz.QuizEntity;

public class QuizSubmissionDTO {
    private Long courseId;
    private List<String> answers;
    private Long quiz;
    private Long StudentId;
    // private List<Boolean> TfAnswers;
    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public Long getStudentId() {
        return StudentId;
    }

    public void setStudentId(Long StudentId) {
        this.StudentId = StudentId;
    }

    public Long getQuiz() {
        return quiz;
    }

    public void setQuiz(Long quizId) {
        this.quiz = quizId;
    }
    @Override
    public String toString(){
        return courseId.toString() + "\n" + answers + "\n" + quiz.toString() + "\n" + StudentId.toString() + "\n" ;
    }

    // public List<Boolean> getTfAnswers() {
    //     return TfAnswers;
    // }

    // public void setTfAnswers(List<Boolean> TfAnswers) {
    //     this.TfAnswers = TfAnswers;
    // }
}
