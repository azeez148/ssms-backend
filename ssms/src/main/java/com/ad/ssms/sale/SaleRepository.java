package com.ad.ssms.sale;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Integer> {

    public List<Sale> findTop5ByOrderByDateDesc();
}