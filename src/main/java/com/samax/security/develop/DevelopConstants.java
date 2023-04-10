package com.samax.security.develop;

import org.springframework.context.annotation.Profile;

@Profile("develop")
public class DevelopConstants {
	public static final String PASSWORD = "Pw_123456";
	public static final String USER_EMAIL = "sample@mail.com";
	public static final String PREMIUM_USER_EMAIL = "premium@mail.com";
	public static final String ADMIN_EMAIL = "admin@mail.com";
}
