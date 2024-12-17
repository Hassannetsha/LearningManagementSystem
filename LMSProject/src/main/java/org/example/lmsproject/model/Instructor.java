package org.example.lmsproject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
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
    // @OneToMany(mappedBy = "instructor")
    // private List<Course> courses;
}
