package com.nexusengine.core.common.exception;

public class InsufficientStockException extends RuntimeException {

    private final Long skuId;

    public InsufficientStockException(Long skuId) {
        super("Insufficient stock for SKU ID: " + skuId);
        this.skuId = skuId;
    }

    public InsufficientStockException(String message) {
        super(message);
        this.skuId = null;
    }

    public Long getSkuId() {
        return skuId;
    }
}
