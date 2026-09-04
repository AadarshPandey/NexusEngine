package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.OmsOrder;
import com.nexusengine.core.model.OmsOrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/9/4.
 */
@Getter
@Setter
public class OmsOrderDetail extends OmsOrder {
    @Schema(title = "Order item list")
    private List<OmsOrderItem> orderItemList;
}
