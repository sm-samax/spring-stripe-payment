package com.samax.security.exception;

import com.samax.security.constants.MessageConstants;

public class PaymentException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public PaymentException() {
		super(MessageConstants.INVALID_PAYMENT_AMOUNT);
	}
}
