package com.nexusengine.core.dto;

import com.nexusengine.core.model.OmsOrder;
import com.nexusengine.core.model.OmsOrderItem;
import com.nexusengine.core.model.OmsOrderOperateHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents the OmsOrderDetail component.
 * Provides core functionality and operations for OmsOrderDetail.
 */
public class OmsOrderDetail extends OmsOrder {
    @Getter
    @Setter
    @Schema(title = "Order item list")
    private List<OmsOrderItem> orderItemList;
    @Getter
    @Setter
    @Schema(title = "History list")
    private List<OmsOrderOperateHistory> historyList;
}
