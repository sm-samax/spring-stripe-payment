package com.samax.security.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.samax.security.enums.Currency;

@Component
public class CurrencyConverter implements Converter<String, Currency>{

	@Override
	public Currency convert(String source) {
		try {
			return Currency.valueOf(source.toUpperCase());			
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}

}
