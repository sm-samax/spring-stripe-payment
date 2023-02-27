package com.samax.security.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.samax.security.constants.MessageConstants;

import lombok.Data;

@Data
public class RegistrationRequest {
	@NotBlank(message = MessageConstants.BLANK_FIELD)
	@Pattern(regexp = "^.*@{1}.*[.]{1}.*$", message = MessageConstants.INVALID_EMAIL_SYNTAX)
	private String email;
	@NotBlank(message = MessageConstants.BLANK_FIELD)
	@Pattern(regexp = "^(?=.*[\\w])(?=.*[@#$%^&+-_=])(?=\\S+$).{8,}$", message = MessageConstants.INVALID_PASSWORD_SYNTAX)
	private String password;
}
