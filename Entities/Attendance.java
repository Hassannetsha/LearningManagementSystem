package com.example.demo.Entities;

import jakarta.persistence.*;

@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for the attendance record

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;  // The lesson for which attendance is being recorded

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;  // The student who attended the lesson

    private boolean isPresent;  // Indicates whether the student attended
    private boolean isEnrolled;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

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
