package org.example.lmsproject.userPart.controller;

import org.example.lmsproject.userPart.model.AuthRequest;
import org.example.lmsproject.userPart.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authenticationService;

    @Autowired
    public AuthController(AuthService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        return authenticationService.verify(authRequest);
    }
}
