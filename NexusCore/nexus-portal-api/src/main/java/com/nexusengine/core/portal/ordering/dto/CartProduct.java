package com.nexusengine.core.portal.ordering.dto;

import com.nexusengine.core.domain.catalog.entity.Product;

import com.nexusengine.core.domain.catalog.entity.AttributeDefinition;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;


/**
 * Represents the CartProduct component.
 * Provides core functionality and operations for CartProduct.
 */
public record CartProduct(
    @JsonUnwrapped Product product,
    @Schema(title = "Product attribute list") List<AttributeDefinition> productAttributeList,
    @Schema(title = "Sku stock list") List<ProductSku> skuStockList
) {
}
