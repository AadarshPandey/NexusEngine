package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.PmsProductAttribute;
import com.nexusengine.core.model.PmsSkuStock;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Represents the CartProduct component.
 * Provides core functionality and operations for CartProduct.
 */
public record CartProduct(
    @JsonUnwrapped PmsProduct pmsProduct,
    @Schema(title = "Product attribute list") List<PmsProductAttribute> productAttributeList,
    @Schema(title = "Sku stock list") List<PmsSkuStock> skuStockList
) {
}
