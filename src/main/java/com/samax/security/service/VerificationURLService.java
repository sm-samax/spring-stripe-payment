package com.samax.security.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samax.security.annotations.ExecuteDaily;
import com.samax.security.constants.VerificationUrlConstants;
import com.samax.security.exception.InvalidVerificationUrlException;
import com.samax.security.model.User;
import com.samax.security.model.VerificationURL;
import com.samax.security.repository.VerificationURLRepository;

@Service
@Transactional
public class VerificationURLService {
	
	@Autowired
	private SecureRandom random;
	
	@Autowired
	private VerificationURLRepository verificationURLRepository;
	
	public String generateVerificationUrl(User user) {
		String url = this.generateRandomUrl();
		
		VerificationURL verificationURL = VerificationURL.builder()
				.url(url)
				.creationTime(Instant.now())
				.user(user)
				.build();
		
		verificationURLRepository.save(verificationURL);
		
		return url;
	}
	
	public User verifyUserWithUrl(String url) {
		VerificationURL verificationURL = verificationURLRepository.findByUrl(url);
		
		if(verificationURL == null || expired(verificationURL)) {
			throw new InvalidVerificationUrlException();
		}
		
		User user = verificationURL.getUser();
		user.setVerified(true);
		
		return user;
	}
	
	@ExecuteDaily
	public void deleteExpiredUrls() {
		verificationURLRepository.findAll().stream()
			.filter(this::expired)
			.forEach(verificationURLRepository::delete);
	}

	private boolean expired(VerificationURL verificationURL) {
		return Instant.now().minus(1L, ChronoUnit.DAYS).isAfter(verificationURL.getCreationTime());
	}
	
	private String generateRandomUrl() {
		StringBuilder stringBuilder = new StringBuilder(VerificationUrlConstants.URL_LENGTH);
		for(int i = 0; i < VerificationUrlConstants.URL_LENGTH; i++) {
			stringBuilder.append(VerificationUrlConstants.URL_ALPHABET
					.charAt(random.nextInt(VerificationUrlConstants.URL_ALPHABET.length())));
		}
		
		String url = stringBuilder.toString();
		
		return verificationURLRepository.findByUrl(url) == null ? url : generateRandomUrl();
	}
}
