package com.ad.ssms.product;

public class ProductUpdateRequest {

    private Long id;
    private int unitPrice;
    private int sellingPrice;
    private boolean isActive;
    private boolean canListed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isCanListed() {
        return canListed;
    }

    public void setCanListed(boolean canListed) {
        this.canListed = canListed;
    }

}
