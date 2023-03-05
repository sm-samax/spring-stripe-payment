package com.samax.security.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.samax.security.constants.MessageConstants;
import com.samax.security.exception.EmailAlreadyInUseException;
import com.samax.security.exception.IncorrectLoginException;
import com.samax.security.exception.InvalidVerificationUrlException;
import com.samax.security.exception.UserNotVerifiedException;
import com.samax.security.model.User;
import com.samax.security.model.dto.LoginRequest;
import com.samax.security.model.dto.RegistrationRequest;
import com.samax.security.service.MailService;
import com.samax.security.service.UserService;
import com.samax.security.service.VerificationURLService;
import com.samax.security.util.MessageUtil;

@RestController
public class AuthenticationController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageUtil messageUtil;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private VerificationURLService verificationURLService;
	
	@GetMapping("/home")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> home() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String name = authentication.getName();
		return ResponseEntity.ok(messageUtil.getMessage(MessageConstants.GREET, name));
	}
	
	@GetMapping("/verify/{url}")
	public ResponseEntity<String> verify(@PathVariable String url) {
		User user = verificationURLService.verifyUserWithUrl(url);
		mailService.sendVerificationSuccessMessage(user);
		return ResponseEntity.ok("Verified!");
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
	
	@GetMapping("/premium")
	@PreAuthorize("hasAuthority('PREMIUM_USER')")
	public ResponseEntity<String> premium() {
		return ResponseEntity.ok("Resource for premium users");
	}
	
	@PostMapping("/register")
	public void registration(@RequestBody @Valid RegistrationRequest registration) {
		userService.registerUser(registration);
	}
		
	@GetMapping("/login")
	public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest) {
		return ResponseEntity.ok(userService.login(loginRequest));
	}
	
	@ExceptionHandler({
		EmailAlreadyInUseException.class,
		IncorrectLoginException.class,
		InvalidVerificationUrlException.class,
		UserNotVerifiedException.class
	})
	public ResponseEntity<String> handleException(Exception ex) {
		String message = messageUtil.getMessage(ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
}
