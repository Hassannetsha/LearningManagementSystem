package org.example.lmsproject.userPart.controller;

//import org.example.lmsproject.model.AuthRequest;
import org.example.lmsproject.userPart.model.User;
// import org.example.lmsproject.model.Admin;
// import org.example.lmsproject.model.Instructor;
// import org.example.lmsproject.model.Student;
import org.example.lmsproject.userPart.service.UserService;
//import org.example.lmsproject.service.JwtService;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
//import org.springframework.security.authentication.AuthenticationManager;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private final UserService service;

    @Autowired
    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/admin/getUsers")
    public String getAllUsers() {
        List<User> users = service.findAllUsers();
        return users.stream().map(User::getUsername).collect(Collectors.joining(", "));
    }

    @GetMapping("/api/getUser/{id}")
    public String getUser(@PathVariable Long id) {
        User user = service.getUser(id);
        return "User: " + user.toString();
    }
}