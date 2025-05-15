package com.ad.ssms.product;

import java.util.Map;

public class UpdateSizeMapRequest {

    private Long productId;
    private Map<String, Integer> sizeMap;

    // Getters and Setters

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Map<String, Integer> getSizeMap() {
        return sizeMap;
    }

    public void setSizeMap(Map<String, Integer> sizeMap) {
        this.sizeMap = sizeMap;
    }
}
