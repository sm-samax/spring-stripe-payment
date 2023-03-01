package com.samax.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samax.security.model.PersistedAuthority;

public interface AuthorityRepository extends JpaRepository<PersistedAuthority, Long> {

	public static final String USER_AUTHORITY = "USER";
	public static final String PREMIUM_USER_AUTHORITY = "PREMIUM_USER";
	public static final String ADMIN_AUTHORITY = "ADMIN";
	
	PersistedAuthority findByAuthority(String authority);
	
	default public PersistedAuthority findUserAuthority() {
		PersistedAuthority userAuthority = this.findByAuthority(USER_AUTHORITY);
		
		return userAuthority != null ? userAuthority : this.save(PersistedAuthority.builder().authority(USER_AUTHORITY).build());
	}

	default public PersistedAuthority findAdminAuthority() {
		PersistedAuthority adminAuthority = this.findByAuthority(ADMIN_AUTHORITY);
		return adminAuthority != null ? adminAuthority : this.save(PersistedAuthority.builder().authority(ADMIN_AUTHORITY).build());
	}

	default public PersistedAuthority findPremiumUserAuthority() {
		PersistedAuthority premiumUserAuthority = this.findByAuthority(PREMIUM_USER_AUTHORITY);
		return premiumUserAuthority != null ? premiumUserAuthority : this.save(PersistedAuthority.builder().authority(PREMIUM_USER_AUTHORITY).build());
	}
}
