package com.samax.security.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailAlreadyInUseException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public EmailAlreadyInUseException(String message) {
		super(message);
	}
}
