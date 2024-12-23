package org.example.lmsproject.assignment.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FeedbackResponse {
    private String feedback;
    private Integer grade;

    public FeedbackResponse(String feedback, Integer grade) {
        this.feedback = feedback;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "FeedbackResponse{" +
                "\n    feedback=" + feedback +
                ",\n    grade=" + grade +
                "\n}";
    }
}
