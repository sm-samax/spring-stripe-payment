package com.samax.security.exception;

import com.samax.security.constants.MessageConstants;

public class UserNotVerifiedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public UserNotVerifiedException() {
		super(MessageConstants.USER_NOT_VERIFIED);
	}
}
