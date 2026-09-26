package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.OmsOrder;
import com.nexusengine.core.model.OmsOrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Represents the OmsOrderDetail component.
 * Provides core functionality and operations for OmsOrderDetail.
 */
public record OmsOrderDetail(
    @JsonUnwrapped OmsOrder omsOrder,
    @Schema(title = "Order item list") List<OmsOrderItem> orderItemList
) {
}
