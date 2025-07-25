package com.ad.ssms.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ad.ssms.purchase.Purchase;
import com.ad.ssms.purchase.PurchaseItem;

@Service
public class PurchaseNotificationService {

    @Autowired
    private WhatsAppNotificationService whatsAppNotificationService;

    @Autowired
    private EmailNotificationService emailNotificationService;

    public void sendPurchaseNotifications(Purchase purchase) {
        // Build messages
        String whatsAppMessage = buildWhatsAppMessage(purchase);
        String emailHtmlMessage = buildEmailMessage(purchase);

        // Send notifications to supplier if contact info is available
        if (purchase.getSupplierMobile() != null && !purchase.getSupplierMobile().isEmpty()) {
            whatsAppNotificationService.enqueueWhatsAppMessage(purchase.getSupplierMobile(), whatsAppMessage);
        }

        if (purchase.getSupplierEmail() != null && !purchase.getSupplierEmail().isEmpty()) {
            emailNotificationService.sendEmail(
                    purchase.getSupplierEmail(),
                    "Purchase Order Confirmation - Order #" + purchase.getId(),
                    emailHtmlMessage
            );
        }

        // Always notify admin
        String adminWhatsAppMessage = "New Purchase Alert!\n" + whatsAppMessage;
        whatsAppNotificationService.enqueueWhatsAppMessage("+917736128108", adminWhatsAppMessage); // TODO: Move to config

        String adminEmailMessage = buildAdminEmailMessage(purchase);
        emailNotificationService.sendEmail(
                "${admin.email}", // Will be replaced with actual admin email from properties
                "New Purchase Order Alert - Order #" + purchase.getId(),
                adminEmailMessage
        );
    }

    private String buildWhatsAppMessage(Purchase purchase) {
        StringBuilder message = new StringBuilder();
        message.append("Purchase Order Confirmation\n\n");
        message.append("Purchase Details:\n");
        message.append("Supplier Name: ").append(purchase.getSupplierName()).append("\n");
        message.append("Total Quantity: ").append(purchase.getTotalQuantity()).append("\n");
        message.append("Total Price: ").append(purchase.getTotalPrice()).append("\n");
        message.append("Date: ").append(purchase.getDate()).append("\n");
        message.append("Payment Type: ").append(purchase.getPaymentType() != null ? purchase.getPaymentType().getName() : "N/A").append("\n");
        message.append("Delivery Type: ").append(purchase.getDeliveryType() != null ? purchase.getDeliveryType().getName() : "N/A").append("\n");
        message.append("\nPurchased Items:\n");

        for (PurchaseItem item : purchase.getPurchaseItems()) {
            message.append("- Product: ").append(item.getProductName())
                    .append(", Size: ").append(item.getSize())
                    .append(", Quantity: ").append(item.getQuantity()).append("\n");
        }

        return message.toString();
    }

    private String buildEmailMessage(Purchase purchase) {
        StringBuilder message = new StringBuilder();
        message.append("<html><body>");
        message.append("<h2>Purchase Order Confirmation</h2>");
        message.append("<h3>Purchase Details:</h3>");
        message.append("<table style='border-collapse: collapse; width: 100%;'>");
        message.append("<tr><td><strong>Order ID:</strong></td><td>").append(purchase.getId()).append("</td></tr>");
        message.append("<tr><td><strong>Supplier Name:</strong></td><td>").append(purchase.getSupplierName()).append("</td></tr>");
        message.append("<tr><td><strong>Total Quantity:</strong></td><td>").append(purchase.getTotalQuantity()).append("</td></tr>");
        message.append("<tr><td><strong>Total Price:</strong></td><td>").append(purchase.getTotalPrice()).append("</td></tr>");
        message.append("<tr><td><strong>Payment Type:</strong></td><td>")
                .append(purchase.getPaymentType() != null ? purchase.getPaymentType().getName() : "N/A").append("</td></tr>");
        message.append("<tr><td><strong>Delivery Type:</strong></td><td>")
                .append(purchase.getDeliveryType() != null ? purchase.getDeliveryType().getName() : "N/A").append("</td></tr>");
        message.append("<tr><td><strong>Date:</strong></td><td>").append(purchase.getDate()).append("</td></tr>");
        message.append("</table>");

        message.append("<h3>Purchased Items:</h3>");
        message.append("<table style='border-collapse: collapse; width: 100%; border: 1px solid #ddd;'>");
        message.append("<tr style='background-color: #f2f2f2;'>");
        message.append("<th style='border: 1px solid #ddd; padding: 8px;'>Product Name</th>");
        message.append("<th style='border: 1px solid #ddd; padding: 8px;'>Size</th>");
        message.append("<th style='border: 1px solid #ddd; padding: 8px;'>Quantity</th>");
        message.append("</tr>");

        for (PurchaseItem item : purchase.getPurchaseItems()) {
            message.append("<tr>");
            message.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(item.getProductName()).append("</td>");
            message.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(item.getSize()).append("</td>");
            message.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(item.getQuantity()).append("</td>");
            message.append("</tr>");
        }
        message.append("</table>");
        message.append("</body></html>");
        return message.toString();
    }

    private String buildAdminEmailMessage(Purchase purchase) {
        StringBuilder message = new StringBuilder();
        message.append("<html><body>");
        message.append("<h2 style='color: #2b5797;'>New Purchase Order Alert!</h2>");
        message.append(buildEmailMessage(purchase).substring("<html><body>".length()));
        return message.toString();
    }
}
