package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the OmsOrderDeliveryParam component.
 * Provides core functionality and operations for OmsOrderDeliveryParam.
 */
public record OmsOrderDeliveryParam(
    @Schema(title = "Order id") Long orderId,
    @Schema(title = "Delivery company") String deliveryCompany,
    @Schema(title = "Delivery sn") String deliverySn
) {
}
