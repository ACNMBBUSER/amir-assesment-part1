package com.example.Book.service;

import com.example.Book.dao.request.SignInRequest;
import com.example.Book.dao.request.SignUpRequest;
import com.example.Book.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {
	
	String signUpAdmin(SignUpRequest request);
	String signUpUser(SignUpRequest request);
	
	JwtAuthenticationResponse signin(SignInRequest request);
;
}
