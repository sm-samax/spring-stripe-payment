package com.samax.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samax.security.model.PersistedAuthority;

public interface AuthorityRepository extends JpaRepository<PersistedAuthority, Long> {

	public static final String USER_AUTHORITY = "USER";
	public static final String ADMIN_AUTHORITY = "ADMIN";
	
	PersistedAuthority findByAuthority(String authority);
	
	default public PersistedAuthority findUserAuthority() {
		PersistedAuthority userAuthority = this.findByAuthority(USER_AUTHORITY);
		return userAuthority != null ? userAuthority : this.save(new PersistedAuthority(null, USER_AUTHORITY, List.of()));
	}

	default public PersistedAuthority findAdminAuthority() {
		PersistedAuthority adminAuthority = this.findByAuthority(ADMIN_AUTHORITY);
		return adminAuthority != null ? adminAuthority : this.save(new PersistedAuthority(null, ADMIN_AUTHORITY, List.of()));
	}
}
