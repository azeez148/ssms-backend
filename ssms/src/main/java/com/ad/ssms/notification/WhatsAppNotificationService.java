package com.ad.ssms.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ad.ssms.sale.Sale;
import com.ad.ssms.sale.SaleItem;

import io.lettuce.core.api.sync.RedisCommands;

@Service
public class WhatsAppNotificationService {

    @Autowired
    private RedisCommands<String, String> redisCommands;

    public void sendSaleNotification(Sale sale) {
        // Send notification to customer
        StringBuilder message = buildSaleMessage(sale);
        enqueueWhatsAppMessage(sale.getCustomerMobile(), message.toString());

        // Send notification to admin
        String adminMessage = "New sale notification!\n" + message.toString();
        enqueueWhatsAppMessage("+917736128108", adminMessage); // TODO: Move admin number to configuration
    }

    public void enqueueWhatsAppMessage(String userPhone, String message) {
        String payload = String.format("{\"phone\": \"%s\", \"message\": \"%s\"}", userPhone, message);
        redisCommands.lpush("whatsapp:queue", payload);
    }

    private StringBuilder buildSaleMessage(Sale sale) {
        StringBuilder message = new StringBuilder();
        message.append("Your sale has been successfully processed. Thank you for your purchase!\n");
        message.append("Sale Details:\n");
        message.append("Customer Name: ").append(sale.getCustomerName()).append("\n");
        message.append("Total Quantity: ").append(sale.getTotalQuantity()).append("\n");
        message.append("Total Price: ").append(sale.getTotalPrice()).append("\n");
        message.append("Payment Type: ").append(sale.getPaymentType() != null ? sale.getPaymentType().getName() : "N/A").append("\n");
        message.append("Delivery Type: ").append(sale.getDeliveryType() != null ? sale.getDeliveryType().getName() : "N/A").append("\n");
        message.append("Date: ").append(sale.getDate()).append("\n");
        message.append("Sale Items:\n");

        for (SaleItem item : sale.getSaleItems()) {
            message.append("Product Name: ").append(item.getProductName()).append(", ");
            message.append("Size: ").append(item.getSize()).append(", ");
            message.append("Quantity: ").append(item.getQuantity()).append("\n");
        }

        message.append("Visit our shop for more products: https://yourshoplink.com\n");
        message.append("Join our WhatsApp group for updates: https://chat.whatsapp.com/test\n");

        return message;
    }
}
