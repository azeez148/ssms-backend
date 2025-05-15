package com.ad.ssms.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Find products by categoryId and product name or description containing the filter
    List<Product> findByCategoryIdAndNameContainingOrCategoryIdAndDescriptionContaining(
        Integer categoryId, String nameFilter, Integer categoryId2, String descriptionFilter);

    // Find products by categoryId
    List<Product> findByCategoryId(Integer categoryId);

    // Find products by name or description containing the filter
    List<Product> findByNameContainingOrDescriptionContaining(String nameFilter, String descriptionFilter);
}
