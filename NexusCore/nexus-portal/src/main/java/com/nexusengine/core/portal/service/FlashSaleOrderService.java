package com.nexusengine.core.portal.service;

import com.nexusengine.core.common.api.CommonResult;

public interface FlashSaleOrderService {
    
    /**
     * Handle flash sale order generation
     * Uses Bucket4j for rate limiting and Redis Lua script for atomic stock deduction
     */
    CommonResult generateFlashOrder(Long productId, Long flashPromotionId, Long flashPromotionSessionId, Integer quantity);
}
