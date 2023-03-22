package com.samax.security.model.dto;

import javax.validation.constraints.NotBlank;

import com.samax.security.constants.MessageConstants;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRequest {
	@NotBlank(message = MessageConstants.BLANK_FIELD)
	private String currency;
	@NotBlank(message = MessageConstants.BLANK_FIELD)
	private String paymentMethodId;
}
