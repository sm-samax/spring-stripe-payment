package com.samax.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samax.security.model.VerificationURL;

@Repository
public interface VerificationURLRepository extends JpaRepository<VerificationURL, Long>{
	VerificationURL findByUrl(String url);
}
