package com.nexusengine.core.portal.catalog.dto;

import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record PromotionProduct(
    @JsonUnwrapped Product product,
    List<ProductSku> skuStockList
) {
}
