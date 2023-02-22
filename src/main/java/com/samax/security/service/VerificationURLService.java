package com.samax.security.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samax.security.exception.InvalidVerificationUrlException;
import com.samax.security.model.User;
import com.samax.security.model.VerificationURL;
import com.samax.security.repository.VerificationURLRepository;

@Service
@Transactional
public class VerificationURLService {
	
	@Autowired
	private VerificationURLRepository verificationURLRepository;
	
	public String generateVerificationUrl(User user) {
		String url = "randomstring";
		
		VerificationURL verificationURL = new VerificationURL(null, url, Instant.now(), user);
		
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

	private boolean expired(VerificationURL verificationURL) {
		return Instant.now().minus(1L, ChronoUnit.DAYS).isAfter(verificationURL.getCreationTime());
	}
}
