package com.nexusengine.core.admin.catalog.dto;

import com.nexusengine.core.domain.catalog.entity.ProductAttributeValue;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import com.nexusengine.core.domain.catalog.entity.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record ProductParam(
    @JsonUnwrapped Product product,
    @Schema(title = "Sku stock list") List<ProductSku> skuStockList,
    @Schema(title = "Product attribute value list") List<ProductAttributeValue> productAttributeValueList
) {
}
