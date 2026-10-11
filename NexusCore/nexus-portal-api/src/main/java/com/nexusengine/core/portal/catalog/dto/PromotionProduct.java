package com.nexusengine.core.portal.catalog.dto;

import com.nexusengine.core.domain.catalog.entity.Product;

import com.nexusengine.core.domain.promotion.entity.SpendThresholdDiscount;
import com.nexusengine.core.domain.promotion.entity.QuantityTierDiscount;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;


/**
 * Represents the PromotionProduct component.
 * Provides core functionality and operations for PromotionProduct.
 */
public record PromotionProduct(
    @JsonUnwrapped Product product,
    List<ProductSku> skuStockList,
    List<QuantityTierDiscount> productVolumeDiscountList,
    List<SpendThresholdDiscount> productSpendDiscountList
) {
}
