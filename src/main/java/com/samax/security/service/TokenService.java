package com.samax.security.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.samax.security.constants.SecurityConstants;
import com.samax.security.model.PersistedAuthority;
import com.samax.security.model.User;

@Service
public class TokenService {

	@Autowired
	private JwtEncoder encoder;
	
	public String generateToken(User user) {
		Instant now = Instant.now();
		
		String scope = user.getAuthorities().stream()
				.map(PersistedAuthority::getAuthority)
				.collect(Collectors.joining(" "));
		
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuedAt(now)
				.expiresAt(now.plus(15, ChronoUnit.MINUTES))
				.issuer(SecurityConstants.ISSUER)
				.subject(user.getEmail())
				.claim("scope", scope)
				.build();
		
		return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}
}
