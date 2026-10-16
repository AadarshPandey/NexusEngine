package com.nexusengine.core.admin.ordering.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record OrderSummaryResponse(
        Long id,
        Long memberId,
        String orderSn,
        OffsetDateTime createTime,
        BigDecimal totalAmount,
        BigDecimal payAmount,
        BigDecimal freightAmount,
        Integer payType,
        Integer status,
        Integer orderType,
        String deliveryCompany,
        String deliverySn,
        String receiverName,
        String receiverPhone,
        Boolean confirmStatus) {}
