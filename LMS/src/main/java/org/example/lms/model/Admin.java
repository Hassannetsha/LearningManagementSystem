package org.example.lms.model;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User{
    public Admin(String username, String password, String email) {
        super(username, password, email, "ADMIN");
    }
    public Admin() {
        super();
    }
    // Add additional methods for admin functionality


}
