package com.samax.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class EnvironmentConfig {

	@Bean
	public Dotenv dotenv() {
		return Dotenv.configure()
			.ignoreIfMissing()
			.ignoreIfMalformed()
			.load();
	}
}
