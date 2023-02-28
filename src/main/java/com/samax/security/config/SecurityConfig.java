package com.samax.security.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.samax.security.constants.SecurityConstants;
import com.samax.security.service.UserService;

import lombok.SneakyThrows;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(SecurityConstants.BCRYPT_STRENGTH);
	}
	
	@Bean 
	public UserDetailsService userDetailsService() {
		return new UserService();
	}
	
	@Bean
	public AuthenticationManager authManager(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(provider);
	}
	
	@Bean
	@SneakyThrows
	public KeyPair keypair() {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(SecurityConstants.RSA);
			keyPairGenerator.initialize(SecurityConstants.RSA_SIZE);
			return keyPairGenerator.generateKeyPair();
	}
	
	@Bean
	public JwtEncoder encoder(KeyPair keypair) {
		JWK jwk = new RSAKey.Builder((RSAPublicKey) keypair.getPublic()).privateKey(keypair.getPrivate()).build();
		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}
	
	@Bean
	public JwtDecoder decoder(KeyPair keypair) {
		return NimbusJwtDecoder.withPublicKey((RSAPublicKey) keypair.getPublic()).build();
	}
	
	@Bean
	public JwtAuthenticationConverter authenticationConverter() {
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		grantedAuthoritiesConverter.setAuthorityPrefix(SecurityConstants.AUTHORITY_PREFIX);
		
		JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
		authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return authenticationConverter;
	}
	
	@Bean
	public CorsFilter cors() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(SecurityConstants.ALLOWED_ORIGINS);
		configuration.setAllowedHeaders(SecurityConstants.ALLOWED_HEADERS);
		configuration.setAllowedMethods(SecurityConstants.ALLOWED_METHODS);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return new CorsFilter(source);
	}
	
	@Bean
	@SneakyThrows
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		return http
				.csrf().disable()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
				.authorizeRequests()
				.antMatchers(SecurityConstants.PUBLIC_URLS)
				.permitAll()
				.anyRequest()
				.authenticated()				
				.and()
				.build();
	}
	
	@Bean
	public SecureRandom random() {
		return new SecureRandom();
	}
}
