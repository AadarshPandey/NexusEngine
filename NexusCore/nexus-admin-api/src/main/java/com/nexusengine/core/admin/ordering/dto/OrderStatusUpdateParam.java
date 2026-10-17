package com.nexusengine.core.admin.ordering.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * Represents the OrderStatusUpdateParam component.
 * Provides core functionality and operations for OrderStatusUpdateParam.
 */
public record OrderStatusUpdateParam(
    @Schema(title = "Id") Long id,
    @JsonAlias({"companyAddressId", "returnAddressId"})
    @Schema(title = "Company address id") Long companyAddressId,
    @JsonAlias({"returnAmount", "refundAmount"})
    @Schema(title = "Return amount") BigDecimal returnAmount,
    @JsonAlias({"handleNote", "reviewNote"})
    @Schema(title = "Handle note") String handleNote,
    @JsonAlias({"handleMan", "reviewedByAdminId"})
    @Schema(title = "Handle man") String handleMan,
    @JsonAlias({"receiveNote", "receiptNote"})
    @Schema(title = "Receive note") String receiveNote,
    @JsonAlias({"receiveMan", "receivedByAdminId"})
    @Schema(title = "Receive man") String receiveMan,
    @Schema(title = "Status") Integer status
) {
}
