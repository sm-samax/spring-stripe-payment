package com.samax.security.model;

import java.time.Instant;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationURL {

	@Id
	@SequenceGenerator(name = "verificationurl_seq_gen", initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verificationurl_seq_gen")
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String url;
	
	@Column(nullable = false)
	private Instant creationTime;
	
	@OneToOne(optional = false, cascade = CascadeType.ALL)
	private User user;
}
