package org.example.lmsproject.userPart.controller;

import java.util.List;

import org.example.lmsproject.userPart.model.AuthRequest;
import org.example.lmsproject.userPart.model.Request;
import org.example.lmsproject.userPart.service.AdminService;
import org.example.lmsproject.userPart.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authenticationService;
    private final AdminService adminService;

    @Autowired
    public AuthController(AuthService authenticationService,AdminService adminService) {
        this.authenticationService = authenticationService;
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        return authenticationService.verify(authRequest);
    }
    @PostMapping("/signup")
    public String signup(@RequestBody Request userRequest) {
        adminService.sendRequest(userRequest);
        return "Request sent\n";
    }
    @GetMapping("/admin/requests")
    public List<Request> getRequests() {
        return adminService.getRequests();
    }
}
