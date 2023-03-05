package com.samax.security.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.samax.security.util.MessageUtil;

@ControllerAdvice
public class GlobalValidationExceptionHandler {

	@Autowired
	private MessageUtil messageUtil;
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleIncorrectRequests(MethodArgumentNotValidException ex) {
		String code = ex.getBindingResult()
				.getFieldErrors()
				.get(0)
				.getDefaultMessage();
		String message = messageUtil.getMessage(code);
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
}
