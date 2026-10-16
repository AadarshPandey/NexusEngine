package com.nexusengine.core.portal.ordering.service;

import com.nexusengine.core.portal.ordering.dto.FlashOrderResponse;

public interface FlashSaleOrderService {

    /**
     * Handle flash sale order generation.
     * Uses Redis Lua script for atomic stock deduction and per-user rate limiting.
     */
    FlashOrderResponse generateFlashOrder(
            Long productId, Long flashPromotionId, Long flashPromotionSessionId, Integer quantity);
}
