package com.samax.security.exception;

import com.samax.security.constants.MessageConstants;

public class EmailAlreadyInUseException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public EmailAlreadyInUseException() {
		super(MessageConstants.INVALID_EMAIL);
	}
}
