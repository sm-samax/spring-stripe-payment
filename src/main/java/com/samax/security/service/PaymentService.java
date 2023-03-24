package com.samax.security.service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samax.security.converter.CurrencyConverter;
import com.samax.security.model.Product;
import com.samax.security.model.dto.PaymentRequest;
import com.samax.security.model.dto.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.SneakyThrows;

@Service
@Transactional
public class PaymentService {

	@Autowired
	private Dotenv dotenv;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CurrencyConverter currencyConverter;
	
	@PostConstruct
	private void init() {
		Stripe.apiKey = dotenv.get("STRIPE_SECRET_KEY");
	}
	
	@SneakyThrows(StripeException.class)
	public void charge(Product product, PaymentRequest payment) {
		PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
				.setCurrency("usd")
				.setAmount(1000L)
				.setPaymentMethod(payment.getPaymentMethodId())
				.build();
		
		PaymentIntent.create(params).confirm();
	}
	
	public PaymentResponse purchasePremiumAuthority(Product product, PaymentRequest payment) {
		this.charge(product, payment);
		String token = userService.grantPremiumUserAuthority();
		
		return PaymentResponse.builder()
				.message("Payment was successfull!")
				.accessToken(token)
				.build();
	}
}
