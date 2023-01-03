package com.samax.security.model.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class RegistrationRequest {
	@NotBlank
	private String email;
	@NotBlank
	private String password;
}
