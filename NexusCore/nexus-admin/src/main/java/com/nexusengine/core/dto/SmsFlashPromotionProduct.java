package com.nexusengine.core.dto;

import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.SmsFlashPromotionProductRelation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Auto-generated documentation
 * Created by macro on 2018/11/16.
 */
public class SmsFlashPromotionProduct extends SmsFlashPromotionProductRelation{
    @Getter
    @Setter
    @Schema(title = "Product")
    private PmsProduct product;
}
