package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * Represents the OmsUpdateStatusParam component.
 * Provides core functionality and operations for OmsUpdateStatusParam.
 */
public record OmsUpdateStatusParam(
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
