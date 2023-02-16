package com.samax.security.constants;

import java.util.List;

public class SecurityConstants {
	public static final String ISSUER = "self";
	public static final String RSA = "RSA";
	public static final String AUTHORITY_PREFIX = "";
	public static final int BCRYPT_STRENGTH  = 10;
	public static final int RSA_SIZE = 2048;
	public static final int EXPIRATION_TIME = 15;
	public static final List<String> ALLOWED_ORIGINS = List.of("http://localhost:3000");
	public static final List<String> ALLOWED_METHODS = List.of("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH");
	public static final List<String> ALLOWED_HEADERS = List.of("*");
	public static final String[] PUBLIC_URLS = {"/register", "/login"};
}