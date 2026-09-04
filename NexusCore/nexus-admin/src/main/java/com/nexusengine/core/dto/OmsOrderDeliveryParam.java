package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Auto-generated documentation
 * Created by macro on 2018/10/12.
 */
@Getter
@Setter
public class OmsOrderDeliveryParam {
    @Schema(title = "Order id")
    private Long orderId;
    @Schema(title = "Delivery company")
    private String deliveryCompany;
    @Schema(title = "Delivery sn")
    private String deliverySn;
}
