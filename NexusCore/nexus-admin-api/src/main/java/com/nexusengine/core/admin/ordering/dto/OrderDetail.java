package com.nexusengine.core.admin.ordering.dto;

import com.nexusengine.core.domain.ordering.entity.CustomerOrder;


import com.nexusengine.core.domain.ordering.entity.OrderLine;
import com.nexusengine.core.domain.ordering.entity.OrderStatusHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonUnwrapped;


/**
 * Represents the OrderDetail component.
 * Provides core functionality and operations for OrderDetail.
 */
public record OrderDetail(
    @JsonUnwrapped CustomerOrder customerOrder,
    @Schema(title = "Order item list") List<OrderLine> orderItemList,
    @Schema(title = "History list") List<OrderStatusHistory> historyList
) {
}
