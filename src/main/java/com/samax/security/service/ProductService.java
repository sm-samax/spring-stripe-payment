package com.samax.security.service;

import org.springframework.stereotype.Service;

import com.samax.security.model.Product;
import com.samax.security.model.dto.PaymentRequest;
import com.samax.security.model.dto.PaymentResponse;
import com.samax.security.repository.ProductRepository;

@Service
public class ProductService {
	
	private ProductRepository productRepository;
	
	private PaymentService paymentService;
	
	public PaymentResponse purchasePremiumAuthority(PaymentRequest payment) {
		Product product = productRepository.findPremiumAuthority();
		return paymentService.purchasePremiumAuthority(product, payment);
	}
}
