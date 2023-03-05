package com.samax.security.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samax.security.constants.PaymentConstants;
import com.samax.security.converter.CurrencyConverter;
import com.samax.security.enums.Currency;
import com.samax.security.exception.PaymentException;
import com.samax.security.model.dto.PaymentRequest;
import com.samax.security.model.dto.PaymentResponse;

@Service
@Transactional
public class PaymentService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CurrencyConverter currencyConverter;
	
	public PaymentResponse purchasePremiumAuthority(PaymentRequest payment) {
		if(!validatePaymentToPremium(payment)) {
			throw new PaymentException();
		}
		
		String token = userService.grantPremiumUserAuthority();
		
		return PaymentResponse.builder()
				.message("Payment was successfull!")
				.accessToken(token)
				.build();
	}
	
	private boolean validatePaymentToPremium(PaymentRequest payment) {
		Currency currency = currencyConverter.convert(payment.getCurrency());
		return PaymentConstants.DEFAULT_CURRENCY.equals(currency);
	}
}
