package com.samax.security.develop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.samax.security.model.User;
import com.samax.security.repository.AuthorityRepository;
import com.samax.security.repository.UserRepository;

@Configuration
@Profile("develop")
public class DevelopConfig {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthorityRepository authorityRepository;
	
	@Bean
	public CommandLineRunner createUser() {
		return args -> {
			User user = User.builder()
					.email(DevelopConstants.USER_EMAIL)
					.password(passwordEncoder.encode(DevelopConstants.PASSWORD))
					.authorities(List.of(authorityRepository.findUserAuthority()))
					.verified(true)
					.build();
			
			userRepository.save(user);
		};
	}
	
	@Bean
	public CommandLineRunner createPremiumUser() {
		return args -> {
			User user = User.builder()
					.email(DevelopConstants.PREMIUM_USER_EMAIL)
					.password(passwordEncoder.encode(DevelopConstants.PASSWORD))
					.authorities(List.of(authorityRepository.findUserAuthority(), authorityRepository.findPremiumUserAuthority()))
					.verified(true)
					.build();
			
			userRepository.save(user);
		};
	}
	
	@Bean
	public CommandLineRunner createAdminUser() {
		return args -> {
			User user = User.builder()
					.email(DevelopConstants.ADMIN_EMAIL)
					.password(passwordEncoder.encode(DevelopConstants.PASSWORD))
					.authorities(List.of(authorityRepository.findUserAuthority(), authorityRepository.findAdminAuthority()))
					.verified(true)
					.build();
			
			userRepository.save(user);
		};
	}
}
