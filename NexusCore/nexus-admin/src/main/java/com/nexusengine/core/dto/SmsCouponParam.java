package com.nexusengine.core.dto;

import com.nexusengine.core.model.SmsCoupon;
import com.nexusengine.core.model.SmsCouponProductCategoryRelation;
import com.nexusengine.core.model.SmsCouponProductRelation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Represents the SmsCouponParam component.
 * Provides core functionality and operations for SmsCouponParam.
 */
public record SmsCouponParam(
    @JsonUnwrapped SmsCoupon smsCoupon,
    @Schema(title = "Product relation list") List<SmsCouponProductRelation> productRelationList,
    @Schema(title = "Product category relation list") List<SmsCouponProductCategoryRelation> productCategoryRelationList
) {
}
