package org.example.lmsproject.userPart.controller;

import org.example.lmsproject.Notification.Services.MailboxService;
import org.example.lmsproject.userPart.model.Response;
import org.example.lmsproject.userPart.model.User;
import org.example.lmsproject.userPart.repository.RequestRepository;
import org.example.lmsproject.userPart.service.AdminService;
import org.example.lmsproject.userPart.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @Autowired
    MailboxService mailboxService;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    ResponseService responseService;

    @PostMapping("/addUser")
    public String addNewUser(@RequestBody User userRequest) {
        User user = adminService.createUserByRole(userRequest);
        String response = adminService.addUser(user);
        return user.getUsername() + " Added Successfully\n" + response;
    }

    @PostMapping("/response")
    public ResponseEntity<String> processResponse(@RequestBody Response response) {
        responseService.processResponse(response);
        return ResponseEntity.ok("Response processed successfully.");
    }

    @PostMapping("/responses")
    public ResponseEntity<String> processAllResponses(@RequestBody int state) {
        String response = responseService.processAllResponses(state);
        return ResponseEntity.ok(response);
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
