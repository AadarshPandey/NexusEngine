package com.nexusengine.core.dto;

import com.nexusengine.core.model.OmsOrder;
import com.nexusengine.core.model.OmsOrderItem;
import com.nexusengine.core.model.OmsOrderOperateHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/10/11.
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
