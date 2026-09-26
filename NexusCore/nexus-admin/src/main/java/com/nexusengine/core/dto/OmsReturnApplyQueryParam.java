package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the OmsReturnApplyQueryParam component.
 * Provides core functionality and operations for OmsReturnApplyQueryParam.
 */
public record OmsReturnApplyQueryParam(
    @Schema(title = "Id") Long id,
    @Schema(title =  "Receiver keyword") String receiverKeyword,
    @Schema(title =  "Status") Integer status,
    @Schema(title =  "Create time") String createTime,
    @Schema(title =  "Handle man") String handleMan,
    @Schema(title =  "Handle time") String handleTime
) {
}
