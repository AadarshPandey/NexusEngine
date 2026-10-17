package com.nexusengine.core.admin.catalog.dto;

import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.entity.ProductAttributeValue;
import com.nexusengine.core.domain.catalog.entity.ProductMedia;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record ProductParam(
    @JsonUnwrapped Product product,
    @Schema(title = "Sku stock list") List<ProductSku> skuStockList,
    @Schema(title = "Product attribute value list") List<ProductAttributeValue> productAttributeValueList,
    @Schema(title = "Gallery media list") List<ProductMedia> galleryList
) {
    public ProductParam(Product product, List<ProductSku> skuStockList, List<ProductAttributeValue> productAttributeValueList) {
        this(product, skuStockList, productAttributeValueList, null);
    }
}
