package com.nexusengine.core.admin.ordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;




/**
 * Represents the OrderDeliveryParam component.
 * Provides core functionality and operations for OrderDeliveryParam.
 */
public record OrderDeliveryParam(
    @Schema(title = "Order id") Long orderId,
    @Schema(title = "Delivery company") String deliveryCompany,
    @Schema(title = "Delivery sn") String deliverySn
) {
}
