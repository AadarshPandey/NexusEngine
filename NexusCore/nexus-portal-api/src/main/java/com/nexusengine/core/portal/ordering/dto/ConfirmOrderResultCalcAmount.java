package com.nexusengine.core.portal.ordering.dto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ConfirmOrderResultCalcAmount(
    @Schema(title = "Total amount") BigDecimal totalAmount,
    @Schema(title = "Freight amount") BigDecimal freightAmount,
    @Schema(title = "Promotion amount") BigDecimal promotionAmount,
    @Schema(title = "Pay amount") BigDecimal payAmount
) {}
