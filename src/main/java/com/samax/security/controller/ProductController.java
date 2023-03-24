package com.samax.security.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.samax.security.constants.MessageConstants;
import com.samax.security.exception.PaymentException;
import com.samax.security.model.dto.PaymentRequest;
import com.samax.security.model.dto.PaymentResponse;
import com.samax.security.service.ProductService;
import com.samax.security.util.MessageUtil;
import com.stripe.exception.StripeException;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private MessageUtil messageUtil;
	
	@PostMapping("/premium")
	@PreAuthorize("!hasAuthority('PREMIUM_USER')")
	public ResponseEntity<PaymentResponse> purchasePremiumUserAuthority(@RequestBody @Valid PaymentRequest payment) {
		return ResponseEntity.ok(productService.purchasePremiumAuthority(payment));
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIncorrectCurrency() {
		String message = messageUtil.getMessage(MessageConstants.INVALID_CURRENCY);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
	
	@ExceptionHandler({PaymentException.class, StripeException.class})
	public ResponseEntity<String> handleIncorrectPayment(PaymentException ex) {
		String message = messageUtil.getMessage(ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
}
