
package com.ad.ssms.dashboard;
import java.util.List;
import java.util.Map;

import com.ad.ssms.purchase.Purchase;
import com.ad.ssms.sale.Sale;

public class Dashboard {
    private List<Sale> recentSales;
    private List<Purchase> recentPurchases;
    private Map<String, Integer> mostSoldItems;
    private int totalSales;
    private double totalRevenue;
    private int totalPurchases;
    private double totalCost;

    public Dashboard() {
    }

    public List<Sale> getRecentSales() {
        return recentSales;
    }
    public void setRecentSales(List<Sale> recentSales) {
        this.recentSales = recentSales;
    }
    public List<Purchase> getRecentPurchases() {
        return recentPurchases;
    }
    public void setRecentPurchases(List<Purchase> recentPurchases) {
        this.recentPurchases = recentPurchases;
    }


    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getTotalPurchases() {
        return totalPurchases;
    }

    public void setTotalPurchases(int totalPurchases) {
        this.totalPurchases = totalPurchases;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    public Map<String, Integer> getMostSoldItems() {
        return mostSoldItems;
    }
    public void setMostSoldItems(Map<String, Integer> mostSoldItems) {
        this.mostSoldItems = mostSoldItems;
    }
}
