package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the OmsOrderQueryParam component.
 * Provides core functionality and operations for OmsOrderQueryParam.
 */
@Getter
@Setter
public class OmsOrderQueryParam {
    @Schema(title =  "Order sn")
    private String orderSn;
    @Schema(title =  "Receiver keyword")
    private String receiverKeyword;
    @Schema(title =  "Status")
    private Integer status;
    @Schema(title =  "Order type")
    private Integer orderType;
    @Schema(title =  "Source type")
    private Integer sourceType;
    @Schema(title =  "Create time")
    private String createTime;
}
