package com.samax.security.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IncorrectLoginException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public IncorrectLoginException(String message) {
		super(message);
	}
}
