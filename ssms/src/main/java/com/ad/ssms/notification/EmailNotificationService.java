package com.ad.ssms.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ad.ssms.sale.Sale;
import com.ad.ssms.sale.SaleItem;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailNotificationService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${admin.email}")
    private String adminEmail;

    public void sendSaleNotification(Sale sale) {
        // Send notification to customer if email is available
        if (sale.getCustomerEmail() != null && !sale.getCustomerEmail().isEmpty()) {
            String subject = "Your Order Confirmation - Order #" + sale.getId();
            String message = buildSaleEmailMessage(sale, false);
            sendEmail(sale.getCustomerEmail(), subject, message);
        }

        // Send notification to admin
        String adminSubject = "New Sale Notification - Order #" + sale.getId();
        String adminMessage = buildSaleEmailMessage(sale, true);
        sendEmail(adminEmail, adminSubject, adminMessage);
    }

    public void sendEmail(String to, String subject, String message) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true); // true indicates HTML content

            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // TODO: Handle exception properly, maybe add to error queue
            e.printStackTrace();
        }
    }

    private String buildSaleEmailMessage(Sale sale, boolean isAdminNotification) {
        StringBuilder message = new StringBuilder();
        message.append("<html><body>");

        if (isAdminNotification) {
            message.append("<h2>New Sale Alert!</h2>");
        } else {
            message.append("<h2>Thank you for your purchase!</h2>");
        }

        message.append("<h3>Sale Details:</h3>");
        message.append("<table style='border-collapse: collapse; width: 100%;'>");
        message.append("<tr><td><strong>Order ID:</strong></td><td>").append(sale.getId()).append("</td></tr>");
        message.append("<tr><td><strong>Customer Name:</strong></td><td>").append(sale.getCustomerName()).append("</td></tr>");
        message.append("<tr><td><strong>Total Quantity:</strong></td><td>").append(sale.getTotalQuantity()).append("</td></tr>");
        message.append("<tr><td><strong>Total Price:</strong></td><td>").append(sale.getTotalPrice()).append("</td></tr>");
        message.append("<tr><td><strong>Payment Type:</strong></td><td>")
                .append(sale.getPaymentType() != null ? sale.getPaymentType().getName() : "N/A").append("</td></tr>");
        message.append("<tr><td><strong>Delivery Type:</strong></td><td>")
                .append(sale.getDeliveryType() != null ? sale.getDeliveryType().getName() : "N/A").append("</td></tr>");
        message.append("<tr><td><strong>Date:</strong></td><td>").append(sale.getDate()).append("</td></tr>");
        message.append("</table>");

        message.append("<h3>Ordered Items:</h3>");
        message.append("<table style='border-collapse: collapse; width: 100%; border: 1px solid #ddd;'>");
        message.append("<tr style='background-color: #f2f2f2;'>");
        message.append("<th style='border: 1px solid #ddd; padding: 8px;'>Product Name</th>");
        message.append("<th style='border: 1px solid #ddd; padding: 8px;'>Size</th>");
        message.append("<th style='border: 1px solid #ddd; padding: 8px;'>Quantity</th>");
        message.append("</tr>");

        for (SaleItem item : sale.getSaleItems()) {
            message.append("<tr>");
            message.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(item.getProductName()).append("</td>");
            message.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(item.getSize()).append("</td>");
            message.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(item.getQuantity()).append("</td>");
            message.append("</tr>");
        }
        message.append("</table>");

        if (!isAdminNotification) {
            message.append("<p><a href='https://yourshoplink.com'>Visit our shop for more products</a></p>");
        }

        message.append("</body></html>");
        return message.toString();
    }
}
