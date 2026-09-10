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
@Getter
@Setter
public class PmsPortalProductDetail{
    @Schema(title = "Product")
    private PmsProduct product;
    @Schema(title = "Brand")
    private PmsBrand brand;
    @Schema(title = "Product attribute list")
    private List<PmsProductAttribute> productAttributeList;
    @Schema(title = "Product attribute value list")
    private List<PmsProductAttributeValue> productAttributeValueList;
    @Schema(title = "Sku stock list")
    private List<PmsSkuStock> skuStockList;
    @Schema(title = "Product ladder list")
    private List<PmsProductLadder> productLadderList;
    @Schema(title = "Product full reduction list")
    private List<PmsProductFullReduction> productFullReductionList;
    @Schema(title = "Coupon list")
    private List<SmsCoupon> couponList;
}
