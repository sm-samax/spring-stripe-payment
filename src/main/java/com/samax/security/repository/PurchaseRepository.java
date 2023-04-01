package com.samax.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samax.security.model.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}
