package org.example.lmsproject.model;

import jakarta.persistence.Entity;
// import jakarta.persistence.ManyToMany;

// import java.util.List;

@Entity
public class Student extends User {

    public Student(String username, String password, String email) {
        super(username, password, email, Role.ROLE_STUDENT);
    }

    public Student() {
        super();
    }

//    @ManyToMany
//    private List<Course> courses;
}