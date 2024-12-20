package org.example.lmsproject.userPart.controller;

import org.example.lmsproject.userPart.model.User;
import org.example.lmsproject.userPart.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @PostMapping("/addUser")
    public String addNewUser(@RequestBody User userRequest) {
        User user = adminService.createUserByRole(userRequest);
        String response = adminService.addUser(user);
        return user.getUsername() + " Added Successfully\n" + response;
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        String response = adminService.updateUser(id, updatedUser);
        return ResponseEntity.ok(response);
    }

    // Endpoint for deleting a User
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        String response = adminService.deleteUser(id);
        return ResponseEntity.ok(response);
    }
}
