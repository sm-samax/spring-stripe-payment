package com.samax.security.service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samax.security.constants.PaymentConstants;
import com.samax.security.converter.CurrencyConverter;
import com.samax.security.enums.SupportedCurrency;
import com.samax.security.model.Product;
import com.samax.security.model.dto.PaymentRequest;
import com.samax.security.model.dto.PaymentResponse;
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
		SupportedCurrency supportedCurrency = currencyConverter.convert(payment.getCurrency());
		String currency = supportedCurrency.toString();
		long amount = exchangeService.getProductPriceIn(product, supportedCurrency);
		String productName = messageUtil.getMessage(product.getProductCode());
		String description = String.format(
				messageUtil.getMessage(PaymentConstants.PAYMENT_DESCRIPTION, productName));
		
		this.charge(description, currency, amount, payment.getPaymentMethodId());
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
}
