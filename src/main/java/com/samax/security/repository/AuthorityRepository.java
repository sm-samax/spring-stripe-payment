package com.samax.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samax.security.model.PersistedAuthority;

public interface AuthorityRepository extends JpaRepository<PersistedAuthority, Long> {

	PersistedAuthority findByAuthority(String authority);
	
	default public PersistedAuthority findUserAuthority() {
		PersistedAuthority userAuthority = findByAuthority("USER");
		return userAuthority != null ? userAuthority : this.save(new PersistedAuthority(null, "USER", List.of()));
	}
}
