package com.samax.security.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.samax.security.exception.EmailAlreadyInUseException;
import com.samax.security.exception.IncorrectLoginException;
import com.samax.security.model.dto.LoginRequest;
import com.samax.security.model.dto.RegistrationRequest;
import com.samax.security.service.UserService;

@RestController
public class AuthenticationController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/home")
	public ResponseEntity<String> hello() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		return ResponseEntity.ok(String.format("Welcome Home %s!", name));
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> registration(@RequestBody @Valid RegistrationRequest registration) {
		return ResponseEntity.ok(userService.registerUser(registration));
	}
		
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest) {
		return ResponseEntity.ok(userService.login(loginRequest));
	}
	
	@ExceptionHandler(EmailAlreadyInUseException.class)
	public ResponseEntity<String> handleEmailAlreadyInUse() {
		String message = "The given email is already being used! Please try with a different one!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
	
	@ExceptionHandler(IncorrectLoginException.class)
	public ResponseEntity<String> handleIncorrectLogin() {
		String message = "The given email or password is incorrect!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleIncorrectRequests() {
		String message = "Some fields are incorrectly filled!";
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
}
