package com.nexusengine.core.portal.service;

import com.nexusengine.core.model.OmsCartItem;
import com.nexusengine.core.portal.domain.CartPromotionItem;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/8/27.
 */
public interface OmsPromotionService {
    /**
     * Auto-generated documentation
     * Auto-generated documentation
     */
    List<CartPromotionItem> calcCartPromotion(List<OmsCartItem> cartItemList);
}
