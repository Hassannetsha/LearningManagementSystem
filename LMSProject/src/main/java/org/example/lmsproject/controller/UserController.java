package org.example.lmsproject.controller;

import org.example.lmsproject.model.User;
import org.example.lmsproject.model.Admin;
import org.example.lmsproject.model.Instructor;
import org.example.lmsproject.model.Student;
import org.example.lmsproject.service.UserService;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private final UserService service;
    @Autowired
    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public String getAllUsers() {
        System.out.println("Endpoint hit: /users");
        List<User> users = service.findAllUsers();
        return "Hiiii";
    }

    @PostMapping("/addUser")
    public String addNewUser(@RequestBody User user) {
        System.out.println("Endpoint hit: /addUser");
        String response = service.addUser(user);
        return user.getUsername() + " Added Successfully\n" + response;
    }

    // Endpoint for adding an Admin
    @PostMapping("/addAdmin")
    public String addAdmin(@RequestBody Admin admin) {
        System.out.println("Endpoint hit: /addAdmin");
        System.out.println("Admin Details: " + admin.toString());
        String response = service.addUser(admin);
        System.out.println("Response: " + response);
        return "Admin " + admin.getUsername() + " Added Successfully\n" + response;
    }

    // Endpoint for adding an Instructor
    @PostMapping("/addInstructor")
    public String addInstructor(@RequestBody Instructor instructor) {
        String response = service.addUser(instructor);
        return "Instructor " + instructor.getUsername() + " Added Successfully\n" + response;
    }

    // Endpoint for adding a Student
    @PostMapping("/addStudent")
    public String addStudent(@RequestBody Student student) {
        String response = service.addUser(student);
        return "Student " + student.getUsername() + " Added Successfully\n" + response;
    }

    @GetMapping("/")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Hello World!";
    }

    @GetMapping("/start")
    public String start() {
        return "Hello World , startt!";
    }




}
