package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.PmsProduct;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Represents the FlashPromotionProduct component.
 * Provides core functionality and operations for FlashPromotionProduct.
 */
public record FlashPromotionProduct(
    @JsonUnwrapped PmsProduct pmsProduct,
    @Schema(title = "Flash promotion price") BigDecimal flashPromotionPrice,
    @Schema(title = "Flash promotion count") Integer flashPromotionCount,
    @Schema(title = "Flash promotion limit") Integer flashPromotionLimit
) {
}
