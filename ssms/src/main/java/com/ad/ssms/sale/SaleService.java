package com.ad.ssms.sale;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ad.ssms.product.Product;
import com.ad.ssms.product.ProductRepository;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;
    
    @Autowired
    private ProductRepository productRepository;

    public Sale saveSale(Sale sale) {
        if (sale.getSaleItems() != null) {
            sale.getSaleItems().forEach(item -> {
                // Find product by id from sale item
                Product product = productRepository.findById(Long.valueOf(item.getProductId())).orElse(null);
                if (product != null && product.getSizeMap() != null) {
                    // Get current quantity (default to 0 if not present)
                    int currentQuantity = product.getSizeMap().getOrDefault(item.getSize(), 0);
                    // Subtract the sale quantity, even if it goes negative
                    int newQuantity = currentQuantity - item.getQuantity();
                    product.getSizeMap().put(item.getSize(), newQuantity);
                    productRepository.save(product);
                }
                // Set the bidirectional relationship
                item.setSale(sale);
            });
        }
        return saleRepository.save(sale);
    }

    public List<Sale> findAllSales() {
        return saleRepository.findAll();
    }
}