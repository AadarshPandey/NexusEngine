package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Auto-generated documentation
 * Created by macro on 2018/10/29.
 */
@Getter
@Setter
public class OmsMoneyInfoParam {
    @Schema(title = "Order id")
    private Long orderId;
    @Schema(title = "Freight amount")
    private BigDecimal freightAmount;
    @Schema(title = "Discount amount")
    private BigDecimal discountAmount;
    @Schema(title = "Status")
    private Integer status;
}
