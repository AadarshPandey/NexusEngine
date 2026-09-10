package com.nexusengine.core.portal.service;

import com.nexusengine.core.model.OmsCartItem;
import com.nexusengine.core.portal.domain.CartPromotionItem;

import java.util.List;

/**
 * Represents the OmsPromotionService component.
 * Provides core functionality and operations for OmsPromotionService.
 */
public interface OmsPromotionService {
        /**
     * Executes the operation.
     * @param cartItemList the cartItemList
     * @return the result of the operation
     */
    List<CartPromotionItem> calcCartPromotion(List<OmsCartItem> cartItemList);
}
