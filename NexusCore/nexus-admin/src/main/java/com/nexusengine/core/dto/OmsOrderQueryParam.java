package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Auto-generated documentation
 * Created by macro on 2018/10/11.
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
