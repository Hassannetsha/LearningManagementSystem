package org.example.lmsproject.userPart.model;

import org.example.lmsproject.course.model.Course;
import jakarta.persistence.*;

import java.util.List;
// import jakarta.persistence.OneToMany;

// import java.util.List;
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Instructor extends User {
    public Instructor(String username, String password, String email) {
        super(username, password, email, Role.ROLE_INSTRUCTOR);
    }

    public Instructor() {
        setRole(Role.ROLE_INSTRUCTOR);
    }
     @OneToMany(mappedBy = "instructor")
     private List<Course> courses;
}
