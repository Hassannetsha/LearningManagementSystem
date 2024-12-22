package org.example.lmsproject.course.model;

import lombok.Getter;
import lombok.Setter;
import org.example.lmsproject.userPart.model.User;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for the attendance record

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;  

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;  

    private boolean isPresent;  
    private boolean isEnrolled;

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
    public boolean isEnrolled() {
        return isEnrolled;
    }

    public void setEnrolled(boolean enrolled) {
        isEnrolled = enrolled;
    }
}
