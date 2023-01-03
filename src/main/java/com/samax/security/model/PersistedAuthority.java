package com.samax.security.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AUTHORITY")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersistedAuthority implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "authority_seq_gen", initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authority_seq_gen")
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String authority;
	
	@ManyToMany(mappedBy = "authorities", cascade = CascadeType.ALL)
	private List<User> users;
	
	@Override
	public String getAuthority() {
		return authority;
	}

}
