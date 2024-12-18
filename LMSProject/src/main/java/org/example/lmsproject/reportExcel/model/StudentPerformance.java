package org.example.lmsproject.reportExcel.model;


public class StudentPerformance {
    private String studentName;
    private String course;
    private double grade;
    private int attendance;

    // Constructors
    public StudentPerformance(String studentName, String course, double grade, int attendance) {
        this.studentName = studentName;
        this.course = course;
        this.grade = grade;
        this.attendance = attendance;
    }

    // Getters and Setters
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }
}

