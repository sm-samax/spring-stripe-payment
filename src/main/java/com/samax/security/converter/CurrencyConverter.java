package com.samax.security.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.samax.security.enums.SupportedCurrency;

@Component
public class CurrencyConverter implements Converter<String, SupportedCurrency>{

	@Override
	public SupportedCurrency convert(String source) {
		try {
			return SupportedCurrency.valueOf(source.toLowerCase());			
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}

}
