package com.ad.ssms.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product findProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public List<Product> getFilteredProducts(Integer categoryId, String productTypeFilter) {
        if (categoryId != null && productTypeFilter != null && !productTypeFilter.isEmpty()) {
            return productRepository.findByCategoryIdAndNameContainingOrCategoryIdAndDescriptionContaining(
                categoryId, productTypeFilter, categoryId, productTypeFilter);
        } else if (categoryId != null) {
            return productRepository.findByCategoryId(categoryId);
        } else if (productTypeFilter != null && !productTypeFilter.isEmpty()) {
            return productRepository.findByNameContainingOrDescriptionContaining(productTypeFilter, productTypeFilter);
        } else {
            return productRepository.findAll();
        }
    }

    // createProduct method
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
}
