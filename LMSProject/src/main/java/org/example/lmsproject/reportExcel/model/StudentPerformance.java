package org.example.lmsproject.reportExcel.model;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudentPerformance {
    private String username;
    @Id
    private Long id;
    private double quizGradePercentage;
    private double attendancePercentage;
    private double assignmentScorePercentage;  // if you want to track assignments

    // Constructor, getters, and setters
    public StudentPerformance(String username,Long id, double quizGradePercentage, double attendancePercentage, double assignmentScorePercentage) {
        this.username = username;
        this.quizGradePercentage = quizGradePercentage;
        this.attendancePercentage = attendancePercentage;
        this.assignmentScorePercentage = assignmentScorePercentage;
        this.id = id;
    }


    public StudentPerformance() {

    }
}
