package com.samax.security.enums;

import lombok.Getter;

@Getter
public enum SupportedCurrency {
	usd, cad, eur, rsd, huf, jpy(false);
	
	private boolean decimal;
	
	private SupportedCurrency() {
		this.decimal = true;
	}
	
	private SupportedCurrency(boolean decimal) {
		this.decimal = decimal;
	}
}
