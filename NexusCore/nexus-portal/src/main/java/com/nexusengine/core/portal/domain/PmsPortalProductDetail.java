package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * Represents the PmsPortalProductDetail{ component.
 * Provides core functionality and operations for PmsPortalProductDetail{.
 */
public record PmsPortalProductDetail(
    @Schema(title = "Product") PmsProduct product,
    @Schema(title = "Brand") PmsBrand brand,
    @Schema(title = "Product attribute list") List<PmsProductAttribute> productAttributeList,
    @Schema(title = "Product attribute value list") List<PmsProductAttributeValue> productAttributeValueList,
    @Schema(title = "Sku stock list") List<PmsSkuStock> skuStockList,
    @Schema(title = "Product ladder list") List<PmsProductVolumeDiscount> productVolumeDiscountList,
    @Schema(title = "Product full reduction list") List<PmsProductSpendDiscount> productSpendDiscountList,
    @Schema(title = "Coupon list") List<SmsCoupon> couponList,
    @Schema(title = "Is a flash sale currently active globally?") Boolean flashSaleActive
) {
}
