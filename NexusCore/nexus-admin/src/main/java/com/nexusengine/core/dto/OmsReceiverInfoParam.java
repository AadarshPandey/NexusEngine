package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the OmsReceiverInfoParam component.
 * Provides core functionality and operations for OmsReceiverInfoParam.
 */
public record OmsReceiverInfoParam(
    @Schema(title =  "Order id") Long orderId,
    @Schema(title =  "Receiver name") String receiverName,
    @Schema(title =  "Receiver phone") String receiverPhone,
    @Schema(title =  "Receiver post code") String receiverPostCode,
    @Schema(title =  "Receiver detail address") String receiverDetailAddress,
    @Schema(title =  "Receiver province") String receiverProvince,
    @Schema(title =  "Receiver city") String receiverCity,
    @Schema(title =  "Receiver region") String receiverRegion,
    @Schema(title =  "Status") Integer status
) {
}
