package com.nexusengine.core.admin.ordering.mapper;

import com.nexusengine.core.admin.ordering.dto.OrderSummaryResponse;
import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderSummaryResponse toSummary(CustomerOrder order);

    List<OrderSummaryResponse> toSummaryList(List<CustomerOrder> orders);
}
