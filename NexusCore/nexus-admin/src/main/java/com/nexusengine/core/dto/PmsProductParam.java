package com.nexusengine.core.dto;

import com.nexusengine.core.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Represents the PmsProductParam component.
 * Provides core functionality and operations for PmsProductParam.
 */

public record PmsProductParam(
    @JsonUnwrapped PmsProduct pmsProduct,
    @Schema(title = "Product ladder list") List<PmsProductVolumeDiscount> productVolumeDiscountList,
    @Schema(title = "Product full reduction list") List<PmsProductSpendDiscount> productSpendDiscountList,
    @Schema(title = "Sku stock list") List<PmsSkuStock> skuStockList,
    @Schema(title = "Product attribute value list") List<PmsProductAttributeValue> productAttributeValueList
) {
}
