package com.nexusengine.core.dto;

import com.nexusengine.core.model.SmsCoupon;
import com.nexusengine.core.model.SmsCouponProductCategoryRelation;
import com.nexusengine.core.model.SmsCouponProductRelation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/8/28.
 */
public class SmsCouponParam extends SmsCoupon {
    @Getter
    @Setter
    @Schema(title = "Product relation list")
    private List<SmsCouponProductRelation> productRelationList;
    @Getter
    @Setter
    @Schema(title = "Product category relation list")
    private List<SmsCouponProductCategoryRelation> productCategoryRelationList;
}
