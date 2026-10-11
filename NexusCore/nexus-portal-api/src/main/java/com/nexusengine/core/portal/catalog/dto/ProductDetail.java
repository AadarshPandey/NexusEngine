package com.nexusengine.core.portal.catalog.dto;

import com.nexusengine.core.domain.catalog.entity.AttributeDefinition;
import com.nexusengine.core.domain.catalog.entity.ProductAttributeValue;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.promotion.entity.Coupon;
import com.nexusengine.core.domain.promotion.entity.SpendThresholdDiscount;
import com.nexusengine.core.domain.promotion.entity.QuantityTierDiscount;
import com.nexusengine.core.domain.catalog.entity.Brand;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;


/**
 * Represents the ProductDetail{ component.
 * Provides core functionality and operations for ProductDetail{.
 */
public record ProductDetail(
    @Schema(title = "Product") Product product,
    @Schema(title = "Brand") Brand brand,
    @Schema(title = "Product attribute list") List<AttributeDefinition> productAttributeList,
    @Schema(title = "Product attribute value list") List<ProductAttributeValue> productAttributeValueList,
    @Schema(title = "Sku stock list") List<ProductSku> skuStockList,
    @Schema(title = "Product ladder list") List<QuantityTierDiscount> productVolumeDiscountList,
    @Schema(title = "Product full reduction list") List<SpendThresholdDiscount> productSpendDiscountList,
    @Schema(title = "Coupon list") List<Coupon> couponList,
    @Schema(title = "Is a flash sale currently active globally?") Boolean flashSaleActive
) {
}
