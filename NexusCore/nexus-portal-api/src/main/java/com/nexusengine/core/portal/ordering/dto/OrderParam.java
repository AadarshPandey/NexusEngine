package com.nexusengine.core.portal.ordering.dto;

import com.nexusengine.core.domain.promotion.entity.Coupon;

import com.nexusengine.core.domain.customer.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;


/**
 * Represents the OrderParam component.
 * Provides core functionality and operations for OrderParam.
 */
public record OrderParam(
    @Schema(title = "Member receive address id") Long memberReceiveAddressId,
    @Schema(title = "Coupon id") Long couponId,
    @Schema(title = "Use integration") Integer usePoints,
    @Schema(title = "Pay type") Integer payType,
    @Schema(title = "Cart ids") List<Long> cartIds
) {
}
