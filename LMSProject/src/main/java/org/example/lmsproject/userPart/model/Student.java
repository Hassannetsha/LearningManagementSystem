package org.example.lmsproject.userPart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id") // Maps the Student's id to the User's id
public class Student extends User {

    public Student(String username, String password, String email) {
        super(username, password, email, Role.ROLE_STUDENT);
    }

    public Student() {
        setRole(Role.ROLE_STUDENT);
    }

    // Additional attributes specific to Student can go here

    // @ManyToMany
    // private List<Course> courses;
}