/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.ad.ssms.dashboard;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ad.ssms.product.Product;
import com.ad.ssms.product.ProductService;
import com.ad.ssms.purchase.PurchaseService;
import com.ad.ssms.sale.SaleService;

@Service
class DashboardService {
    // This service class would typically interact with the repository layer to
    // fetch data
    // For now, we can just return a dummy Dashboard object

    @Autowired
    private SaleService salesService;
    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ProductService productService;

    public Dashboard getDashboardData() {
        Dashboard dashboard = new Dashboard();

        // get recent sales data using sales service
        dashboard.setRecentSales(salesService.getRecentSales());
        // get recent purchases data using purchase service
        dashboard.setRecentPurchases(purchaseService.getRecentPurchases());
        // get most sold items data using sales service
        dashboard.setMostSoldItems(salesService.getMostSoldItems());
        // get total sales using sales service
        dashboard.setTotalSales(salesService.getTotalSales());
        // get total revenue using sales service
        dashboard.setTotalRevenue(getTotalRevenue());
        // get total purchases using purchase service
        dashboard.setTotalPurchases(purchaseService.getTotalPurchases());

        // get total items in stock using stock service
        // Calculate total stock quantity by summing quantities from each product's size
        // map
        int totalStockQuantity = productService.findAllProducts().stream()
                .mapToInt(product -> product.getSizeMap().values().stream().mapToInt(Integer::intValue).sum())
                .sum();
        dashboard.setTotalItemsInStock(totalStockQuantity);

        // Calculate total stock value using unit price multiplied by each product's
        // total quantity
        double totalStockValue = productService.findAllProducts().stream()
                .mapToDouble(product -> product.getUnitPrice() *
                        product.getSizeMap().values().stream().mapToInt(Integer::intValue).sum())
                .sum();

        dashboard.setTotalStockValue(totalStockValue);

        Map<String, Integer> categoryItemCounts = productService.findAllProducts().stream()
                .collect(Collectors.groupingBy(
                        product -> product.getCategory().getName().toString(),
                        Collectors.summingInt(
                                product -> product.getSizeMap().values().stream().mapToInt(Integer::intValue).sum())));
        dashboard.setCategoryItemCounts(categoryItemCounts);

        // Add more data as needed
        return dashboard;
    }

    private double getTotalRevenue() {
        return salesService.getTotalSales() - purchaseService.getTotalPurchases();
    }
}
