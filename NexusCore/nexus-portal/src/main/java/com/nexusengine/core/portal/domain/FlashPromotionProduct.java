package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.PmsProduct;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Represents the FlashPromotionProduct component.
 * Provides core functionality and operations for FlashPromotionProduct.
 */
@Getter
@Setter
public class FlashPromotionProduct extends PmsProduct{
    @Schema(title = "Flash promotion price")
    private BigDecimal flashPromotionPrice;
    @Schema(title = "Flash promotion count")
    private Integer flashPromotionCount;
    @Schema(title = "Flash promotion limit")
    private Integer flashPromotionLimit;
}
