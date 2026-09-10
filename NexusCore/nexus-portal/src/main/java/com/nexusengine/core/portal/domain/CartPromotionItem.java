package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.OmsCartItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Represents the CartPromotionItem component.
 * Provides core functionality and operations for CartPromotionItem.
 */
@Getter
@Setter
public class CartPromotionItem extends OmsCartItem{
    @Schema(title = "Promotion message")
    private String promotionMessage;
    @Schema(title = "Reduce amount")
    private BigDecimal reduceAmount;
    @Schema(title = "Real stock")
    private Integer realStock;
    @Schema(title = "Integration")
    private Integer integration;
    @Schema(title = "Growth")
    private Integer growth;
}
