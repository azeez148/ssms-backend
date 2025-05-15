package com.ad.ssms.product;

public class ProductFilterRequest {
    private Integer categoryId;
    private String productTypeFilter;

    // Getters and Setters
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductTypeFilter() {
        return productTypeFilter;
    }

    public void setProductTypeFilter(String productTypeFilter) {
        this.productTypeFilter = productTypeFilter;
    }
}
