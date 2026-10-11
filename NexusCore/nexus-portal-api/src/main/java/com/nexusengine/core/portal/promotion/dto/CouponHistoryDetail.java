package com.nexusengine.core.portal.promotion.dto;

import com.nexusengine.core.domain.catalog.entity.Category;

import com.nexusengine.core.domain.catalog.entity.Product;

import com.nexusengine.core.domain.promotion.entity.Coupon;
import com.nexusengine.core.domain.promotion.entity.MemberCoupon;
import com.nexusengine.core.domain.promotion.entity.CouponCategory;
import com.nexusengine.core.domain.promotion.entity.CouponProduct;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;


/**
 * Represents the CouponHistoryDetail component.
 * Provides core functionality and operations for CouponHistoryDetail.
 */
public record CouponHistoryDetail(
    @JsonUnwrapped MemberCoupon memberCoupon,
    @Schema(title = "Coupon") Coupon coupon,
    @Schema(title = "Product relation list") List<CouponProduct> productRelationList,
    @Schema(title = "Category relation list") List<CouponCategory> categoryRelationList
) {
}
