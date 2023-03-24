package com.samax.security.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.samax.security.constants.PaymentConstants;
import com.samax.security.enums.SupportedCurrency;
import com.samax.security.model.Product;

import lombok.Cleanup;
import lombok.SneakyThrows;

@Service
public class ExchangeService {

	private Map<SupportedCurrency, BigDecimal> exchangeRate = loadExchangeRate();
	
	public long getProductPriceIn(Product product, SupportedCurrency currency) {
		long price;
		
		if(!PaymentConstants.NATIVE_CURRENCY.equals(currency))
		{
			price = exchange(product.getNativePrice(), currency);
		} else {
			price = product.getNativePrice().longValue();			
		}
		
		if(currency.isDecimal()) {
			return price * 100;
		}
		
		return price;
	}
	
	private long exchange(BigDecimal nativePrice, SupportedCurrency currency) {
		return exchangeRate.get(currency)
				.multiply(nativePrice)
				.longValue();
	}
	
	@SneakyThrows
	private Map<SupportedCurrency, BigDecimal> loadExchangeRate() {
		Map<SupportedCurrency, BigDecimal> exchangeRate = new HashMap<>();
		
		@Cleanup
		BufferedReader reader = new BufferedReader(new FileReader(
				new ClassPathResource("exchange_rate.csv").getFile()));
		
		String[] currenciesRaw = reader.readLine().split(",");
		String[] rateRaw = reader.readLine().split(",");
		
		for(int i = 0; i < currenciesRaw.length; i++) {
			SupportedCurrency currency = SupportedCurrency.valueOf(currenciesRaw[i]);
			BigDecimal rate = new BigDecimal(rateRaw[i]);
			exchangeRate.put(currency, rate);
		}
		
		return exchangeRate;
	}
}
