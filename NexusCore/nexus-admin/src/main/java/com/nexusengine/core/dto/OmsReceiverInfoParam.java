package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Auto-generated documentation
 * Created by macro on 2018/10/29.
 */
@Getter
@Setter
public class OmsReceiverInfoParam {
    @Schema(title =  "Order id")
    private Long orderId;
    @Schema(title =  "Receiver name")
    private String receiverName;
    @Schema(title =  "Receiver phone")
    private String receiverPhone;
    @Schema(title =  "Receiver post code")
    private String receiverPostCode;
    @Schema(title =  "Receiver detail address")
    private String receiverDetailAddress;
    @Schema(title =  "Receiver province")
    private String receiverProvince;
    @Schema(title =  "Receiver city")
    private String receiverCity;
    @Schema(title =  "Receiver region")
    private String receiverRegion;
    @Schema(title =  "Status")
    private Integer status;
}
