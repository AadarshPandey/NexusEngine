package com.nexusengine.core.admin.ordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;


import lombok.Getter;
import lombok.Setter;


/**
 * Represents the OrderQueryParam component.
 * Provides core functionality and operations for OrderQueryParam.
 */
public record OrderQueryParam(
    @Schema(title =  "Order sn") String orderSn,
    @Schema(title =  "Receiver keyword") String receiverKeyword,
    @Schema(title =  "Status") Integer status,
    @Schema(title =  "Order type") Integer orderType,
    @Schema(title =  "Source type") Integer sourceType,
    @Schema(title =  "Create time") String createTime
) {
}
