package com.samax.security.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samax.security.exception.EmailAlreadyInUseException;
import com.samax.security.exception.IncorrectLoginException;
import com.samax.security.model.PersistedAuthority;
import com.samax.security.model.User;
import com.samax.security.model.dto.LoginRequest;
import com.samax.security.model.dto.RegistrationRequest;
import com.samax.security.model.mapper.UserMapper;
import com.samax.security.repository.AuthorityRepository;
import com.samax.security.repository.UserRepository;

@Service
@Transactional
public class UserService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthorityRepository authorityRepository;
	
	@Autowired
	private UserMapper mapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private TokenService tokenService;
	
	public String registerUser(RegistrationRequest registration) {
		if(userRepository.findByEmail(registration.getEmail()) != null) {
			throw new EmailAlreadyInUseException();
		}
		
		String encodedPassword = passwordEncoder.encode(registration.getPassword());
		List<PersistedAuthority> authorities = List.of(authorityRepository.findUserAuthority());
		User user = userRepository.save(mapper.toUser(registration, encodedPassword, authorities));
		return tokenService.generateToken(user);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		return mapper.toUserDetails(user);
	}

	public String login(LoginRequest login) {
		User userFromDatabase = userRepository.findByEmail(login.getEmail());
		if(userFromDatabase == null || !passwordEncoder.matches(login.getPassword(), userFromDatabase.getPassword())) {
			throw new IncorrectLoginException();
		}
		return tokenService.generateToken(userFromDatabase);
	}
}
