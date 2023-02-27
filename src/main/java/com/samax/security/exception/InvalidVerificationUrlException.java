package com.samax.security.exception;

import com.samax.security.constants.MessageConstants;

public class InvalidVerificationUrlException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InvalidVerificationUrlException() {
		super(MessageConstants.INVALID_VERIFICATION_URL);
	}
}
