package com.samax.security.model.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.samax.security.constants.MessageConstants;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRequest {
	@NotBlank(message = MessageConstants.BLANK_FIELD)
	private String currency;
	@NotNull(message = MessageConstants.BLANK_FIELD)
	@Min(value =  0, message = MessageConstants.AMOUNT_UNDER_MINIMUM)
	private BigDecimal amount;
	@NotBlank(message = MessageConstants.BLANK_FIELD)
	private String email;
	@NotBlank(message = MessageConstants.BLANK_FIELD)
	private String token;
}
