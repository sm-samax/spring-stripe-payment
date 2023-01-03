package com.samax.security.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.samax.security.model.PersistedAuthority;
import com.samax.security.model.User;
import com.samax.security.model.dto.RegistrationRequest;

@Mapper(componentModel = "spring")
public interface UserMapper {
	default UserDetails toUserDetails(User user) {
		return org.springframework.security.core.userdetails.User.builder()
				.username(user.getEmail())
				.password(user.getPassword())
				.authorities(user.getAuthorities())
				.build();
	}
	
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "encodedPassword", target = "password")
	User toUser(RegistrationRequest registration, String encodedPassword, List<PersistedAuthority> authorities);
}
