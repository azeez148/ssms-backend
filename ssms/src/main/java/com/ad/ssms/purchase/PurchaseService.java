package com.ad.ssms.purchase;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ad.ssms.product.Product;
import com.ad.ssms.product.ProductRepository;
import com.ad.ssms.sale.Sale;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;
    
    @Autowired
    private ProductRepository productRepository;

    public Purchase savePurchase(Purchase purchase) {
        if (purchase.getPurchaseItems() != null) {
            purchase.getPurchaseItems().forEach(item -> {
                // For a purchase, add the incoming quantity to the product's sizeMap.
                Product product = productRepository.findById(Long.valueOf(item.getProductId())).orElse(null);
                if (product != null && product.getSizeMap() != null) {
                    int currentQuantity = product.getSizeMap().getOrDefault(item.getSize(), 0);
                    int newQuantity = currentQuantity + item.getQuantity();
                    product.getSizeMap().put(item.getSize(), newQuantity);
                    productRepository.save(product);
                }
                // Set the bidirectional relationship
                item.setPurchase(purchase);
            });
        }
        return purchaseRepository.save(purchase);
    }

    public List<Purchase> findAllPurchases() {
        return purchaseRepository.findAll();
    }

    public List<Purchase> getRecentPurchases() {
        return purchaseRepository.findTop5ByOrderByDateDesc();
    }

    public int getTotalPurchases() {
                return (int) purchaseRepository.findAll().stream()
                .mapToDouble(Purchase:: getTotalPrice)
                .sum();
    }
}