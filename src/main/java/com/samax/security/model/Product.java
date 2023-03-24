package com.samax.security.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	@Id
	@SequenceGenerator(name = "product_seq_gen", initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq_gen")
	private Long id;
	private String productCode;
	private BigDecimal nativePrice;
}
