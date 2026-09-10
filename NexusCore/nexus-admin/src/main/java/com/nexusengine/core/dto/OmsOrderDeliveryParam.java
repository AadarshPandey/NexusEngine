package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the OmsOrderDeliveryParam component.
 * Provides core functionality and operations for OmsOrderDeliveryParam.
 */
@Getter
@Setter
public class OmsOrderDeliveryParam {
    @Schema(title = "Order id")
    private Long orderId;
    @Schema(title = "Delivery company")
    private String deliveryCompany;
    @Schema(title = "Delivery sn")
    private String deliverySn;
}
