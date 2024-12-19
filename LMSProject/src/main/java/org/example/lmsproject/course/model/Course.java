package org.example.lmsproject.course.model;

import org.example.lmsproject.userPart.model.*;
import org.example.lmsproject.assignment.model.Assignment;
import jakarta.persistence.*;
import org.springframework.expression.spel.ast.Assign;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long courseId;
    private String title;
    private String description;
    private int duration;
    private boolean available;

    @ManyToOne
    Instructor instructor;
    @ManyToMany
    List<Student> students;
    @OneToMany(mappedBy = "course")
    List<Assignment> assignments;
    @OneToMany(mappedBy = "course")
    List <Lesson> lessons;
//    @OneToMany
//    List<Quiz> quizzes;

    public Course(String title, String description, int duration, boolean available,  Instructor instructor) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.instructor = instructor;
    }

    public Course() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
    }

    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
    }

    public void removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
    }

    public void addAssignment(Assignment assignment) {
        this.assignments.add(assignment);
    }

    public void removeAssignment(Assignment assignment) {
        this.assignments.remove(assignment);
    }



    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", instructor=" + instructor +
                ", students=" + students +
                '}';
    }
}
