package com.samax.security.controller;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

import com.samax.security.exception.EmailAlreadyInUseException;
import com.samax.security.exception.IncorrectLoginException;
import com.samax.security.model.dto.LoginRequest;
import com.samax.security.model.dto.RegistrationRequest;
import com.samax.security.service.UserService;

@RestController
public class AuthenticationController {
	
	private static final String INVALID_LOGIN = "invalid.login";
	private static final String INVALID_EMAIL = "invalid.email";
	private static final String INVALID_FIELDS = "invalid.fields";
	private static final String GREET = "greet";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("/home")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> hello() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String name = authentication.getName();
		Locale locale = LocaleContextHolder.getLocale();
		return ResponseEntity.ok(messageSource.getMessage(GREET, new Object[]{name}, locale));
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
		String message = messageSource.getMessage(INVALID_EMAIL, null, LocaleContextHolder.getLocale());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
	
	@ExceptionHandler(IncorrectLoginException.class)
	public ResponseEntity<String> handleIncorrectLogin() {
		String message = messageSource.getMessage(INVALID_LOGIN, null, LocaleContextHolder.getLocale());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleIncorrectRequests() {
		String message = messageSource.getMessage(INVALID_FIELDS, null, LocaleContextHolder.getLocale());
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
}
