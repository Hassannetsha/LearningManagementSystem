package org.example.lms.model;

import jakarta.persistence.Entity;
// import jakarta.persistence.OneToMany;

// import java.util.List;
@Entity
public class Instructor extends User{
    Instructor(String username, String password, String email) {
        super(username, password, email, "INSTRUCTOR");
    }
    public Instructor() {
        super();
    }
//    @OneToMany(mappedBy = "instructor")
//    private List<Course> courses;
}
