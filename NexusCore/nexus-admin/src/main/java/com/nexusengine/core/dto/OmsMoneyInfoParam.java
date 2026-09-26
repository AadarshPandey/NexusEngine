package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * Represents the OmsMoneyInfoParam component.
 * Provides core functionality and operations for OmsMoneyInfoParam.
 */
public record OmsMoneyInfoParam(
    @Schema(title = "Order id") Long orderId,
    @Schema(title = "Freight amount") BigDecimal freightAmount,
    @Schema(title = "Discount amount") BigDecimal discountAmount,
    @Schema(title = "Status") Integer status
) {
}
