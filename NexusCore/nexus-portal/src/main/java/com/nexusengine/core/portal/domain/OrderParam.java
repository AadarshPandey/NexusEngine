package com.nexusengine.core.portal.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/8/30.
 */
@Data
@EqualsAndHashCode
public class OrderParam {
    @Schema(title = "Member receive address id")
    private Long memberReceiveAddressId;
    @Schema(title = "Coupon id")
    private Long couponId;
    @Schema(title = "Use integration")
    private Integer useIntegration;
    @Schema(title = "Pay type")
    private Integer payType;
    @Schema(title = "Cart ids")
    private List<Long> cartIds;
}
