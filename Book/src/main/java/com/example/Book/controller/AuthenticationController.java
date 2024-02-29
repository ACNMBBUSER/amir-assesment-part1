package com.example.Book.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Book.dao.request.SignUpRequest;
import com.example.Book.dao.request.SignInRequest;
import com.example.Book.dao.response.JwtAuthenticationResponse;
import com.example.Book.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    
    @PostMapping("/signup/admin")
    public ResponseEntity<String> signupAdmin(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signUpAdmin(request));
    }
    
    @PostMapping("/signup/user")
    public ResponseEntity<String> signupUser(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signUpUser(request));
    }
    
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }
}