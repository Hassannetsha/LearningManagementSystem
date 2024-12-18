package org.example.lmsproject.userPart.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// import java.lang.reflect.Type;

@Setter
@Getter
@Entity
@Table
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    public enum Role {
        ROLE_ADMIN, ROLE_INSTRUCTOR, ROLE_STUDENT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING) // Store enum values as strings
    private Role role;

    public User(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
