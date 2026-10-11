package com.nexusengine.core.portal.ordering.dto;

import com.nexusengine.core.domain.ordering.entity.CartItem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonUnwrapped;


/**
 * Represents the CartPromotionItem component.
 * Provides core functionality and operations for CartPromotionItem.
 */
public record CartPromotionItem(
    @JsonUnwrapped CartItem cartItem,
    @Schema(title = "Promotion message") String promotionMessage,
    @Schema(title = "Reduce amount") BigDecimal reduceAmount,
    @Schema(title = "Real stock") Integer realStock,
    @Schema(title = "Integration") Integer earnedPoints
) {
}
