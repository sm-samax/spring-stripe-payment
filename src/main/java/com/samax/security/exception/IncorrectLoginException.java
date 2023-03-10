package com.samax.security.exception;

import com.samax.security.constants.MessageConstants;

public class IncorrectLoginException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public IncorrectLoginException() {
		super(MessageConstants.INVALID_LOGIN);
	}
}
