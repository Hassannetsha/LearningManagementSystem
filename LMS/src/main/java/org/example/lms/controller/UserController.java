package org.example.lms.controller;

import org.example.lms.model.User;
import org.example.lms.service.UserService;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserService service;

    @PostMapping("/signUp")
    public String addNewUser(@RequestBody User user) {
        String response = service.addUser(user);
        return user.getUsername() + " Added Successfully\n" + response;
    }

    @GetMapping("/hello")
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
