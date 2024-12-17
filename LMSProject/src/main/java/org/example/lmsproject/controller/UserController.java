package org.example.lmsproject.controller;

//import org.example.lmsproject.model.AuthRequest;
import org.example.lmsproject.model.User;
// import org.example.lmsproject.model.Admin;
// import org.example.lmsproject.model.Instructor;
// import org.example.lmsproject.model.Student;
import org.example.lmsproject.service.UserService;
//import org.example.lmsproject.service.JwtService;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
//import org.springframework.security.authentication.AuthenticationManager;


import java.util.List;
import java.util.stream.Collectors;

@RestController
// @CrossOrigin(origins = "*")
public class UserController {

    private final UserService service;
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtService jwtService;

    @Autowired
    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/api/getUsers")
    public String getAllUsers() {
        List<User> users = service.findAllUsers();
        return users.stream().map(User::getUsername).collect(Collectors.joining(", "));
    }

    // @PostMapping("/api/addUser")
    // public String addNewUser(@RequestBody User user) {
    // System.out.println("Endpoint hit: /addUser");
    // String response = service.addUser(user);
    // return user.getUsername() + " Added Successfully\n" + response;
    // }

    @PostMapping("/admin/addUser")
    public String addNewUser(@RequestBody User userRequest) {
        User user = service.createUserByRole(userRequest);
        String response = service.addUser(user);
        return user.getUsername() + " Added Successfully\n" + response;
    }

    // Endpoint for adding an Admin
    // @PostMapping("/api/addAdmin")
    // public String addAdmin(@RequestBody Admin admin) {
    // System.out.println("Endpoint hit: /addAdmin");
    // System.out.println("Admin Details: " + admin.toString());
    // String response = service.addUser(admin);
    // System.out.println("Response: " + response);
    // return "Admin " + admin.getUsername() + " Added Successfully\n" + response;
    // }
    //
    // // Endpoint for adding an Instructor
    // @PostMapping("/api/addInstructor")
    // public String addInstructor(@RequestBody Instructor instructor) {
    // String response = service.addUser(instructor);
    // return "Instructor " + instructor.getUsername() + " Added Successfully\n" +
    // response;
    // }
    //
    // // Endpoint for adding a Student
    // @PostMapping("/api/addStudent")
    // public String addStudent(@RequestBody Student student) {
    // String response = service.addUser(student);
    // return "Student " + student.getUsername() + " Added Successfully\n" +
    // response;
    // }

    @GetMapping("/api/getUser/{id}")
    public String getUser(@PathVariable Long id) {
        User user = service.getUser(id);
        return "User: " + user.toString();
    }

    // Endpoint for updating a User
    @PutMapping("/api/updateUser/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        String response = service.updateUser(id, updatedUser);
        return ResponseEntity.ok(response);
    }

    // Endpoint for deleting a User
    @DeleteMapping("/api/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        String response = service.deleteUser(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public String hello() {
        return "Hello World!";
    }

    // @GetMapping("/admin")
    // public String admin() {
    // return "Hello World!";
    // }
    //
    // @GetMapping("/start")
    // public String start() {
    // return "Hello World , startt!";
    // }

//    @PostMapping("/login")
//    public String login(@RequestBody AuthRequest authRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
//        );
//        if (authentication.isAuthenticated()) {
//            return jwtService.generateToken(authRequest.getUsername());
//        } else {
//            throw new UsernameNotFoundException("Invalid user request!");
//        }
//    }

}
