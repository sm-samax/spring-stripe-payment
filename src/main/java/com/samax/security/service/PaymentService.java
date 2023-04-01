package com.samax.security.service;

import java.time.Instant;
import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samax.security.constants.PaymentConstants;
import com.samax.security.converter.CurrencyConverter;
import com.samax.security.enums.SupportedCurrency;
import com.samax.security.model.Product;
import com.samax.security.model.Purchase;
import com.samax.security.model.dto.PaymentRequest;
import com.samax.security.model.dto.PaymentResponse;
import com.samax.security.repository.PurchaseRepository;
import com.samax.security.util.MessageUtil;
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
	private CurrencyConverter currencyConverter;
	
	@Autowired
	private ExchangeService exchangeService;
	
	@Autowired
	private MessageUtil messageUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PurchaseRepository purchaseRepository;
	
	@PostConstruct
	private void init() {
		Stripe.apiKey = dotenv.get("STRIPE_SECRET_KEY");
	}
	
	public PaymentResponse purchase(Product product, PaymentRequest payment) {
		this.confirmPurchase(product, payment);
		
		return PaymentResponse.builder()
				.message(messageUtil.getMessage(PaymentConstants.PAYMENT_SUCCESS))
				.build();
	}
	
	private void confirmPurchase(Product product, PaymentRequest payment) {
		SupportedCurrency currency = currencyConverter.convert(payment.getCurrency());
		long amount = exchangeService.getProductPriceIn(product, currency);
		String productName = messageUtil.getMessage(product.getProductCode());
		String description = String.format(
				messageUtil.getMessage(PaymentConstants.PAYMENT_DESCRIPTION, productName));
		
		this.charge(description, currency.toString(), amount, payment.getPaymentMethodId());
		
		this.savePurchase(product, currency);
	}
	
	@SneakyThrows(StripeException.class)
	private void charge(String description, String currency, long amount, String paymentMethodId) {
		PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
				.setDescription(description)
				.setCurrency(currency)
				.setAmount(amount)
				.setPaymentMethod(paymentMethodId)
				.build();
		
		PaymentIntent.create(params).confirm();
	}
	
	private void savePurchase(Product product, SupportedCurrency currency) {
		Purchase purchase = Purchase.builder()
			.products(Collections.singletonList(product))
			.currency(currency)
			.date(Instant.now())
			.user(userService.currentUser())
			.build();
		
		purchaseRepository.save(purchase);
	}
}
