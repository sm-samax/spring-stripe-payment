package com.samax.security.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.samax.security.constants.MessageConstants;
import com.samax.security.exception.EmailAlreadyInUseException;
import com.samax.security.exception.IncorrectLoginException;
import com.samax.security.model.dto.LoginRequest;
import com.samax.security.model.dto.RegistrationRequest;
import com.samax.security.service.UserService;
import com.samax.security.util.MessageUtil;

@RestController
public class AuthenticationController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageUtil messageUtil;
	
	@GetMapping("/home")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> home() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String name = authentication.getName();
		return ResponseEntity.ok(messageUtil.getMessage(MessageConstants.GREET, name));
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<String> user() {
		return ResponseEntity.ok("Resource for users");
	}
	
	@GetMapping("/admin")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> admin() {
		return ResponseEntity.ok("Resource for admins");
	}
	
	@PostMapping("/admin")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> grantAdminAuthority() {
		return ResponseEntity.ok(userService.grantAdminAuthority());
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
		String message = messageUtil.getMessage(MessageConstants.INVALID_EMAIL);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
	
	@ExceptionHandler(IncorrectLoginException.class)
	public ResponseEntity<String> handleIncorrectLogin() {
		String message = messageUtil.getMessage(MessageConstants.INVALID_LOGIN);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleIncorrectRequests() {
		String message = messageUtil.getMessage(MessageConstants.INVALID_FIELDS);
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
}
