package org.example.lmsproject.reportExcel.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class StudentPerformance {
    private String username;
    @Id
    private Long id;
    private double quizGrade;
    private double attendancePercentage;
    private double assignmentScore;  // if you want to track assignments

    // Constructor, getters, and setters
    public StudentPerformance(String username,Long id, double quizGrade, double attendancePercentage, double assignmentScore) {
        this.username = username;
        this.quizGrade = quizGrade;
        this.attendancePercentage = attendancePercentage;
        this.assignmentScore = assignmentScore;
        this.id = id;
    }


    public StudentPerformance() {

    }
}
