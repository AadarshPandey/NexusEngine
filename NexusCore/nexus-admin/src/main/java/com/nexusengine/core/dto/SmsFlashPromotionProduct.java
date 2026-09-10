package com.nexusengine.core.dto;

import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.SmsFlashPromotionProductRelation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the SmsFlashPromotionProduct component.
 * Provides core functionality and operations for SmsFlashPromotionProduct.
 */
public class SmsFlashPromotionProduct extends SmsFlashPromotionProductRelation{
    @Getter
    @Setter
    @Schema(title = "Product")
    private PmsProduct product;
}
