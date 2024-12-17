package org.example.lmsproject.controller;

import org.example.lmsproject.model.AuthRequest;
import org.example.lmsproject.service.AuthService;
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
