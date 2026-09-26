package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.SmsCoupon;
import com.nexusengine.core.model.SmsCouponHistory;
import com.nexusengine.core.model.SmsCouponProductCategoryRelation;
import com.nexusengine.core.model.SmsCouponProductRelation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Represents the SmsCouponHistoryDetail component.
 * Provides core functionality and operations for SmsCouponHistoryDetail.
 */
public record SmsCouponHistoryDetail(
    @JsonUnwrapped SmsCouponHistory smsCouponHistory,
    @Schema(title = "Coupon") SmsCoupon coupon,
    @Schema(title = "Product relation list") List<SmsCouponProductRelation> productRelationList,
    @Schema(title = "Category relation list") List<SmsCouponProductCategoryRelation> categoryRelationList
) {
}
