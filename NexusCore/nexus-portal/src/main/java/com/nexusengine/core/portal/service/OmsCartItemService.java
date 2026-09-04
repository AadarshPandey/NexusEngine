package com.nexusengine.core.portal.service;

import com.nexusengine.core.model.OmsCartItem;
import com.nexusengine.core.portal.domain.CartProduct;
import com.nexusengine.core.portal.domain.CartPromotionItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/8/2.
 */
public interface OmsCartItemService {
    /**
     * Auto-generated documentation
     */
    @Transactional
    int add(OmsCartItem cartItem);

    /**
     * Auto-generated documentation
     */
    List<OmsCartItem> list(Long memberId);

    /**
     * Auto-generated documentation
     */
    List<CartPromotionItem> listPromotion(Long memberId, List<Long> cartIds);

    /**
     * Auto-generated documentation
     */
    int updateQuantity(Long id, Long memberId, Integer quantity);

    /**
     * Auto-generated documentation
     */
    int delete(Long memberId,List<Long> ids);

    /**
     *Auto-generated documentation
     */
    CartProduct getCartProduct(Long productId);

    /**
     * Auto-generated documentation
     */
    @Transactional
    int updateAttr(OmsCartItem cartItem);

    /**
     * Auto-generated documentation
     */
    int clear(Long memberId);
}
