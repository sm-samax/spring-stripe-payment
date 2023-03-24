package com.samax.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samax.security.constants.ProductConstants;
import com.samax.security.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	Product findByProductCode(String productCode);
	
	default Product findPremiumAuthority() {
		Product premiumAuthority = this.findByProductCode(ProductConstants.PREMIUM_AUTHORITY_CODE);
		return premiumAuthority != null ? premiumAuthority : this.save(
				Product.builder()
					.productCode(ProductConstants.PREMIUM_AUTHORITY_CODE)
					.nativePrice(ProductConstants.PREMIUM_AUTHORITY_NATIVE_PRICE)
					.build());
	}
}
