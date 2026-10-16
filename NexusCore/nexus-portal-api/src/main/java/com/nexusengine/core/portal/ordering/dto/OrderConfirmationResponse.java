package com.nexusengine.core.portal.ordering.dto;

import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import com.nexusengine.core.domain.ordering.entity.OrderLine;
import java.util.List;

public record OrderConfirmationResponse(CustomerOrder order, List<OrderLine> orderItemList) {}
