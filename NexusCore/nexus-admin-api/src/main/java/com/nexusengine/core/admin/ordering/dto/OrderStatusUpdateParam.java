package com.nexusengine.core.admin.ordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;


import java.math.BigDecimal;


/**
 * Represents the OrderStatusUpdateParam component.
 * Provides core functionality and operations for OrderStatusUpdateParam.
 */
public record OrderStatusUpdateParam(
    @Schema(title = "Id") Long id,
    @Schema(title = "Company address id") Long companyAddressId,
    @Schema(title = "Return amount") BigDecimal returnAmount,
    @Schema(title = "Handle note") String handleNote,
    @Schema(title = "Handle man") String handleMan,
    @Schema(title = "Receive note") String receiveNote,
    @Schema(title = "Receive man") String receiveMan,
    @Schema(title = "Status") Integer status
) {
}
