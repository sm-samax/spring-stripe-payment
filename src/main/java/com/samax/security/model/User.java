package com.samax.security.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@SequenceGenerator(name = "user_seq_gen", initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	private boolean verified;
	
	@ManyToMany
	private List<PersistedAuthority> authorities;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Purchase> purchases;
}
