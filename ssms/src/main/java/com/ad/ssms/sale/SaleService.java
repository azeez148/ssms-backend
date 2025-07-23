package com.ad.ssms.sale;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private com.ad.ssms.notification.WhatsAppNotificationService whatsAppNotificationService;

    @Autowired
    private com.ad.ssms.notification.EmailNotificationService emailNotificationService;

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

        // Save the sale first
        Sale savedSale = saleRepository.save(sale);

        // Send notifications
        whatsAppNotificationService.sendSaleNotification(savedSale);
        emailNotificationService.sendSaleNotification(savedSale);

        return savedSale;

    }

    public List<Sale> findAllSales() {
        return saleRepository.findAll();
    }

    public List<Sale> getRecentSales() {
        return saleRepository.findTop5ByOrderByDateDesc();
    }

    public Map<String, Integer> getMostSoldItems() {
        // Aggregate sale item quantities by product name from all sales
        List<Sale> sales = saleRepository.findAll();
        Map<String, Integer> productSales = new HashMap<>();
        for (Sale sale : sales) {
            if (sale.getSaleItems() != null) {
                for (SaleItem item : sale.getSaleItems()) {
                    productSales.merge(item.getProductName(), item.getQuantity(), Integer::sum);
                }
            }
        }
        return productSales;
    }

    public int getTotalSales() {
        // Sum the total price from all sales
        return (int) saleRepository.findAll().stream()
                .mapToDouble(Sale::getTotalPrice)
                .sum();
    }

}
