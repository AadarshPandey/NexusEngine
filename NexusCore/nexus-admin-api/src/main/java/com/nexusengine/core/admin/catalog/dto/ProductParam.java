package com.nexusengine.core.admin.catalog.dto;

import com.nexusengine.core.domain.promotion.entity.SpendThresholdDiscount;
import com.nexusengine.core.domain.catalog.entity.ProductAttributeValue;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import com.nexusengine.core.domain.promotion.entity.QuantityTierDiscount;
import com.nexusengine.core.domain.catalog.entity.Product;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;


/**
 * Represents the ProductParam component.
 * Provides core functionality and operations for ProductParam.
 */

public record ProductParam(
    @JsonUnwrapped Product product,
    @Schema(title = "Product ladder list") List<QuantityTierDiscount> productVolumeDiscountList,
    @Schema(title = "Product full reduction list") List<SpendThresholdDiscount> productSpendDiscountList,
    @Schema(title = "Sku stock list") List<ProductSku> skuStockList,
    @Schema(title = "Product attribute value list") List<ProductAttributeValue> productAttributeValueList
) {
}
