/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.ad.ssms.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ad.ssms.purchase.PurchaseService;
import com.ad.ssms.sale.SaleService;

@Service
class DashboardService {
    // This service class would typically interact with the repository layer to fetch data
    // For now, we can just return a dummy Dashboard object

    @Autowired
    private SaleService salesService;
    @Autowired
    private PurchaseService purchaseService;

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

        // Add more data as needed
        return dashboard;
    }   

    private double getTotalRevenue() {
        return salesService.getTotalSales() - purchaseService.getTotalPurchases();
    }
}
