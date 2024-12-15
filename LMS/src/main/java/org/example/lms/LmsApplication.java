package org.example.lms;

import org.example.lms.model.User;
import org.example.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LmsApplication {

    // private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public static void main(String[] args) {
        SpringApplication.run(LmsApplication.class, args);
    }

    //    @Bean
    @Bean
    public CommandLineRunner commandLineRunner(UserService userService) {
        return args -> {
            // Check if the database is empty (no users exist)
            if (userService.findAllUsers().isEmpty()) {
                // Add the initial user
                User user = new User();
                user.setUsername("admin");  // Set the username
                user.setPassword(passwordEncoder.encode("zinab27"));  // Set the password (encoded)
                user.setRole("ADMIN");  // Set the role
                userService.addUser(user);  // Save the user to the database
                System.out.println("Initial admin user added.");
            } else {
                System.out.println("Users already exist in the database.");
            }
        };
    }
}