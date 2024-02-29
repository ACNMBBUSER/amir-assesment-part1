package com.example.author.service;

import com.example.author.dao.request.SignUpRequest;
import com.example.author.dao.request.SigninRequest;
import com.example.author.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {

	String signupAdmin(SignUpRequest request);
	String signupUser(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}
