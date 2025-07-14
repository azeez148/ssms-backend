package com.ad.ssms.purchase;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    public List<Purchase> findTop5ByOrderByDateDesc();
}