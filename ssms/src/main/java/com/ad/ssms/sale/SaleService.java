package com.ad.ssms.sale;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ad.ssms.product.Product;
import com.ad.ssms.product.ProductRepository;

import io.lettuce.core.api.sync.RedisCommands;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RedisCommands<String, String> redisCommands;

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

        // need to trigger the whatsapp 

        StringBuilder message = getMessage(sale);

        enqueueWhatsAppMessage(sale.getCustomerMobile(), message.toString());

        return saleRepository.save(sale);
    }

    private StringBuilder getMessage(Sale sale) {
        // construct the message
        StringBuilder message = new StringBuilder();
        message.append("Your sale has been successfully processed. Thank you for your purchase!\n");
        message.append("Sale Details:\n");
        message.append("Customer Name: ").append(sale.getCustomerName()).append("\n");
        // need sale amount
        message.append("Total Quantity: ").append(sale.getTotalQuantity()).append("\n");
        message.append("Total Price: ").append(sale.getTotalPrice()).append("\n");
        // need date
        message.append("Payment Type: ").append(sale.getPaymentType() != null ? sale.getPaymentType().getName() : "N/A").append("\n");
        message.append("Delivery Type: ").append(sale.getDeliveryType() != null ? sale.getDeliveryType().getName() : "N/A").append("\n");
        message.append("Date: ").append(sale.getDate()).append("\n");
        message.append("Sale Items:\n");
        for (SaleItem item : sale.getSaleItems()) {
            // productName
            message.append("Product Name: ").append(item.getProductName()).append(", ");
            message.append("Size: ").append(item.getSize()).append(", ");
            message.append("Quantity: ").append(item.getQuantity()).append("\n");
        }

        // add a shop link to the mesasage for the promotion purpose
        message.append("Visit our shop for more products: https://yourshoplink.com\n");
        // add a wgatsapp group link to the message for the promotion purpose
        message.append("Join our WhatsApp group for updates: https://chat.whatsapp.com/test\n");
        return message;
    }

    public List<Sale> findAllSales() {
        return saleRepository.findAll();
    }

    
public void enqueueWhatsAppMessage(String userPhone, String message) {
    String payload = String.format("{\"phone\": \"%s\", \"message\": \"%s\"}", userPhone, message);
    redisCommands.lpush("whatsapp:queue", payload);  // Use LPUSH / RPUSH
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