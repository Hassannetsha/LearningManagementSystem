package org.example.lmsproject.quiz.DTOs.Questions;

import java.util.List;

public class QuestionBankDTO {
    // private Long id;
    private Long courseId;
    private String questionBankName;
    private List<Long> questionIds;

    // public Long getId() {
    //     return id;
    // }

    // public void setId(Long id) {
    //     this.id = id;
    // }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public List<Long> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<Long> questionIds) {
        this.questionIds = questionIds;
    }

    public String getQuestionBankName() {
        return questionBankName;
    }

    public void setQuestionBankName(String QuizName) {
        this.questionBankName = QuizName;
    }
}