package org.example.lmsproject.userPart.model;

import aj.org.objectweb.asm.ConstantDynamic;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import org.example.lmsproject.course.model.Course;

import java.util.List;

@Getter
@Entity
@PrimaryKeyJoinColumn(name = "id") // Maps the Student's id to the User's id
public class Student extends User {

    @ManyToMany
    private List<Course> courses;

    public Student(String username, String password, String email) {
        super(username, password, email, Role.ROLE_STUDENT);
    }

    public Student() {
        setRole(Role.ROLE_STUDENT);
    }


    // Additional attributes specific to Student can go here


}