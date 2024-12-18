package org.example.lmsproject;

import org.example.lmsproject.userPart.model.Admin;
import org.example.lmsproject.userPart.model.User;
import org.example.lmsproject.userPart.service.AdminService;
import org.example.lmsproject.userPart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
// import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LmsProjectApplication {

    @Autowired
    // private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(LmsProjectApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserService userService, AdminService adminService) {
        return args -> {
            // Check if the database is empty (no users exist)
            if (userService.findAllUsers().isEmpty()) {
                // Add the initial user
                User user = new Admin();
                user.setUsername("admin2"); // Set the username
                user.setEmail("firstAdmin@gmail.com"); // Set the email
                user.setPassword("zinab27"); // Set the password (encoded)
                // user.setRole(User.Role.ROLE_ADMIN); // Set the role
                adminService.addUser(user); // Save the user to the database
                System.out.println("Initial admin user added.");
            } else {
                System.out.println("Users already exist in the database.");
            }
        };
    }
}