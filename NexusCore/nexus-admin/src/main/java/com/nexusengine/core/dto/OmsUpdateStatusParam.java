package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Represents the OmsUpdateStatusParam component.
 * Provides core functionality and operations for OmsUpdateStatusParam.
 */
@Getter
@Setter
public class OmsUpdateStatusParam {
    @Schema(title = "Id")
    private Long id;
    @Schema(title = "Company address id")
    private Long companyAddressId;
    @Schema(title = "Return amount")
    private BigDecimal returnAmount;
    @Schema(title = "Handle note")
    private String handleNote;
    @Schema(title = "Handle man")
    private String handleMan;
    @Schema(title = "Receive note")
    private String receiveNote;
    @Schema(title = "Receive man")
    private String receiveMan;
    @Schema(title = "Status")
    private Integer status;
}
