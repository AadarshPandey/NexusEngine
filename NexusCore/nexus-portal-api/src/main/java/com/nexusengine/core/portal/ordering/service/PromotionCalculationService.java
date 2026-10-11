package com.nexusengine.core.portal.ordering.service;

import com.nexusengine.core.domain.ordering.entity.CartItem;


import java.util.List;
import com.nexusengine.core.portal.ordering.dto.CartPromotionItem;


/**
 * Represents the PromotionCalculationService component.
 * Provides core functionality and operations for PromotionCalculationService.
 */
public interface PromotionCalculationService {
        /**
     * Executes the operation.
     * @param cartItemList the cartItemList
     * @return the result of the operation
     */
    List<CartPromotionItem> calcCartPromotion(List<CartItem> cartItemList);
}
