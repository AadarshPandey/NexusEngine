package com.nexusengine.core.portal.promotion.dto;

import com.nexusengine.core.domain.catalog.entity.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonUnwrapped;


/**
 * Represents the FlashPromotionProduct component.
 * Provides core functionality and operations for FlashPromotionProduct.
 */
public record FlashPromotionProduct(
    @JsonUnwrapped Product product,
    @Schema(title = "Flash promotion price") BigDecimal flashPromotionPrice,
    @Schema(title = "Flash promotion count") Integer flashPromotionCount,
    @Schema(title = "Flash promotion limit") Integer flashPromotionLimit
) {
}
