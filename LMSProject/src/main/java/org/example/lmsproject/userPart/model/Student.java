package org.example.lmsproject.userPart.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.lmsproject.course.model.CourseEnrollRequest;
import org.example.lmsproject.course.model.Course;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@PrimaryKeyJoinColumn(name = "id") // Maps the Student's id to the User's id
public class Student extends User {

    @ManyToMany
    private List<Course> courses;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @Setter
    @Getter
    private List<CourseEnrollRequest> courseEnrollRequests;

    public Student(String username, String password, String email) {
        super(username, password, email, Role.ROLE_STUDENT);
        this.courses = new ArrayList<Course>();
        this.courseEnrollRequests = new ArrayList<>();
    }

    public Student() {
        setRole(Role.ROLE_STUDENT);
    }


    // Additional attributes specific to Student can go here


}