package com.nexusengine.core.portal.catalog.dto;

import com.nexusengine.core.domain.catalog.entity.AttributeDefinition;
import com.nexusengine.core.domain.catalog.entity.Brand;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.entity.ProductAttributeValue;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import com.nexusengine.core.domain.promotion.entity.Coupon;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record ProductDetail(
    @Schema(title = "Product") Product product,
    @Schema(title = "Brand") Brand brand,
    @Schema(title = "Product attribute list") List<AttributeDefinition> productAttributeList,
    @Schema(title = "Product attribute value list") List<ProductAttributeValue> productAttributeValueList,
    @Schema(title = "Sku stock list") List<ProductSku> skuStockList,
    @Schema(title = "Coupon list") List<Coupon> couponList
) {
}
