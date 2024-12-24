package org.example.lmsproject.quiz.DTOs.Quizzes;

import java.util.List;


public class QuizSubmissionDTO {
    private Long courseId;
    private List<String> answers;
    private Long quiz;
    // private Long StudentId;
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

    // public Long getStudentId() {
    //     return StudentId;
    // }

    // public void setStudentId(Long StudentId) {
    //     this.StudentId = StudentId;
    // }

    public Long getQuiz() {
        return quiz;
    }

    public void setQuiz(Long quizId) {
        this.quiz = quizId;
    }
    @Override
    public String toString(){
        return courseId.toString() + "\n" + answers + "\n" + quiz.toString() + "\n" ;
    }
}
