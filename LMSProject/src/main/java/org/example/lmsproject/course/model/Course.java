package org.example.lmsproject.course.model;

import org.example.lmsproject.userPart.model.*;
import jakarta.persistence.*;
import org.hibernate.sql.ast.tree.update.Assignment;

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

    @ManyToOne
    Instructor instructor;
    @ManyToMany
    List<Student> students;
//    @OneToMany
//    List<Assignment> assignments;
//    @OneToMany
//    List<Quiz> quizzes;

//    public Course() {
//        this.title = null;
//        this.description = null;
//        this.duration = 0;
//        this.instructor = null;
//        this.students = new ArrayList<>();
//    }

    public Course(String title, String description, int duration, Instructor instructor, List<Student> students) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.instructor = instructor;
        this.students = students;
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

    public void addStudent(Student student) {
        this.students.add(student);
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
