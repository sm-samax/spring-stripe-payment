package com.samax.security.model;

import java.time.Instant;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.samax.security.enums.SupportedCurrency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Purchase {
	@Id
	@SequenceGenerator(name = "purchase_seq_gen", initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_seq_gen")
	private Long id;
	private Instant date;
	
	@Enumerated(EnumType.STRING)
	private SupportedCurrency currency;
	
	@ManyToOne(optional = false)
	private User user;
	
	@ManyToMany
	private List<Product> products;
	
}
