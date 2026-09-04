package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.SmsCoupon;
import com.nexusengine.core.model.SmsCouponHistory;
import com.nexusengine.core.model.SmsCouponProductCategoryRelation;
import com.nexusengine.core.model.SmsCouponProductRelation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/8/29.
 */
@Getter
@Setter
public class SmsCouponHistoryDetail extends SmsCouponHistory {
    @Schema(title = "Coupon")
    private SmsCoupon coupon;
    @Schema(title = "Product relation list")
    private List<SmsCouponProductRelation> productRelationList;
    @Schema(title = "Category relation list")
    private List<SmsCouponProductCategoryRelation> categoryRelationList;
}
