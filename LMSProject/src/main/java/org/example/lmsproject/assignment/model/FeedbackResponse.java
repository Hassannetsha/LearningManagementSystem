package org.example.lmsproject.assignment.model;

public class FeedbackResponse {
    private String feedback;
    private Integer grade;

    public FeedbackResponse(String feedback, Integer grade) {
        this.feedback = feedback;
        this.grade = grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
}
