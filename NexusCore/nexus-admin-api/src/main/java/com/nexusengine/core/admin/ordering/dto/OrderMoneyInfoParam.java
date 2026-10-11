package com.nexusengine.core.admin.ordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;


import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;


/**
 * Represents the OrderMoneyInfoParam component.
 * Provides core functionality and operations for OrderMoneyInfoParam.
 */
public record OrderMoneyInfoParam(
    @Schema(title = "Order id") Long orderId,
    @Schema(title = "Freight amount") BigDecimal freightAmount,
    @Schema(title = "Discount amount") BigDecimal discountAmount,
    @Schema(title = "Status") Integer status
) {
}
