package com.nexusengine.core.admin.ordering.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import com.nexusengine.core.domain.ordering.entity.OrderLine;
import com.nexusengine.core.domain.ordering.entity.OrderStatusHistory;
import com.nexusengine.core.domain.payment.entity.PaymentTransaction;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Represents the OrderDetail component.
 * Provides core functionality and operations for OrderDetail.
 */
public record OrderDetail(
    @JsonUnwrapped CustomerOrder customerOrder,
    @Schema(title = "Order item list") List<OrderLine> orderItemList,
    @Schema(title = "History list") List<OrderStatusHistory> historyList,
    @Schema(title = "Payment transactions") List<PaymentTransaction> paymentTransactions
) {
}
