package com.nexusengine.core.portal.ordering.dto;

import com.nexusengine.core.domain.ordering.entity.ReturnRequest;


import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import com.nexusengine.core.domain.ordering.entity.OrderLine;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;


/**
 * Represents the OrderDetail component.
 * Provides core functionality and operations for OrderDetail.
 */
public record OrderDetail(
    @JsonUnwrapped CustomerOrder customerOrder,
    @Schema(title = "Order item list") List<OrderLine> orderItemList,
    @Schema(title = "Return apply list") List<ReturnRequest> returnApplyList
) {
}
