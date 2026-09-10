package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the OmsReturnApplyQueryParam component.
 * Provides core functionality and operations for OmsReturnApplyQueryParam.
 */
@Getter
@Setter
public class OmsReturnApplyQueryParam {
    @Schema(title = "Id")
    private Long id;
    @Schema(title =  "Receiver keyword")
    private String receiverKeyword;
    @Schema(title =  "Status")
    private Integer status;
    @Schema(title =  "Create time")
    private String createTime;
    @Schema(title =  "Handle man")
    private String handleMan;
    @Schema(title =  "Handle time")
    private String handleTime;
}
