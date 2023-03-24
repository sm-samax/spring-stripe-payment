package com.samax.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samax.security.model.Product;
import com.samax.security.model.dto.PaymentRequest;
import com.samax.security.model.dto.PaymentResponse;
import com.samax.security.repository.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private UserService userService;
	
	public PaymentResponse purchasePremiumAuthority(PaymentRequest payment) {
		Product product = productRepository.findPremiumAuthority();
		PaymentResponse response = paymentService.purchase(product, payment);
		response.setAccessToken(userService.grantPremiumUserAuthority());
		return response;
	}
}
