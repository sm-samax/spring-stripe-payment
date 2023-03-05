package com.samax.security.enums;

import lombok.Getter;

@Getter
public enum Currency {
	USD, CAD, EUR, RSD, HUF(false), JPY(false);
	
	private boolean decimal;
	
	private Currency() {
		this.decimal = true;
	}
	
	private Currency(boolean decimal) {
		this.decimal = decimal;
	}
}
