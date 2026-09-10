package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.OmsOrder;
import com.nexusengine.core.model.OmsOrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents the OmsOrderDetail component.
 * Provides core functionality and operations for OmsOrderDetail.
 */
@Getter
@Setter
public class OmsOrderDetail extends OmsOrder {
    @Schema(title = "Order item list")
    private List<OmsOrderItem> orderItemList;
    @Schema(title = "Return apply list")
    private List<com.nexusengine.core.model.OmsOrderReturnApply> returnApplyList;
}
