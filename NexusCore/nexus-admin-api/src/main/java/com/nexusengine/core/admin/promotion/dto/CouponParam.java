package com.nexusengine.core.admin.promotion.dto;




import com.nexusengine.core.domain.promotion.entity.Coupon;
import com.nexusengine.core.domain.promotion.entity.CouponCategory;
import com.nexusengine.core.domain.promotion.entity.CouponProduct;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;


/**
 * Represents the CouponParam component.
 * Provides core functionality and operations for CouponParam.
 */
public record CouponParam(
    @JsonUnwrapped Coupon coupon,
    @Schema(title = "Product relation list") List<CouponProduct> productRelationList,
    @Schema(title = "Product category relation list") List<CouponCategory> productCategoryRelationList
) {
}
