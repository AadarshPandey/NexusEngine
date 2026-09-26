package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the OmsOrderQueryParam component.
 * Provides core functionality and operations for OmsOrderQueryParam.
 */
public record OmsOrderQueryParam(
    @Schema(title =  "Order sn") String orderSn,
    @Schema(title =  "Receiver keyword") String receiverKeyword,
    @Schema(title =  "Status") Integer status,
    @Schema(title =  "Order type") Integer orderType,
    @Schema(title =  "Source type") Integer sourceType,
    @Schema(title =  "Create time") String createTime
) {
}
