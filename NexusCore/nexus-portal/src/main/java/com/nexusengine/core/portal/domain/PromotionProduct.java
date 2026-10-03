package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.PmsProductSpendDiscount;
import com.nexusengine.core.model.PmsProductVolumeDiscount;
import com.nexusengine.core.model.PmsSkuStock;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Represents the PromotionProduct component.
 * Provides core functionality and operations for PromotionProduct.
 */
public record PromotionProduct(
    @JsonUnwrapped PmsProduct pmsProduct,
    List<PmsSkuStock> skuStockList,
    List<PmsProductVolumeDiscount> productVolumeDiscountList,
    List<PmsProductSpendDiscount> productSpendDiscountList
) {
}
