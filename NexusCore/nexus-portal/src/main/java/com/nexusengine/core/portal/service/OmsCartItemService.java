package com.nexusengine.core.portal.service;

import com.nexusengine.core.model.OmsCartItem;
import com.nexusengine.core.portal.domain.CartProduct;
import com.nexusengine.core.portal.domain.CartPromotionItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Represents the OmsCartItemService component.
 * Provides core functionality and operations for OmsCartItemService.
 */
public interface OmsCartItemService {
        /**
     * Executes the operation.
     * @param cartItem the cartItem
     * @return the result of the operation
     */
    @Transactional
    int add(OmsCartItem cartItem);

        /**
     * Executes the operation.
     * @param memberId the memberId
     * @return the result of the operation
     */
    List<OmsCartItem> list(Long memberId);

        /**
     * Executes the operation.
     * @param memberId the memberId
     * @param cartIds the cartIds
     * @return the result of the operation
     */
    List<CartPromotionItem> listPromotion(Long memberId, List<Long> cartIds);

        /**
     * Executes the operation.
     * @param id the id
     * @param memberId the memberId
     * @param quantity the quantity
     * @return the result of the operation
     */
    int updateQuantity(Long id, Long memberId, Integer quantity);

        /**
     * Executes the operation.
     * @param memberId the memberId
     * @param ids the ids
     * @return the result of the operation
     */
    int delete(Long memberId,List<Long> ids);

        /**
     * Executes the operation.
     * @param productId the productId
     * @return the result of the operation
     */
    CartProduct getCartProduct(Long productId);

        /**
     * Executes the operation.
     * @param cartItem the cartItem
     * @return the result of the operation
     */
    @Transactional
    int updateAttr(OmsCartItem cartItem);

        /**
     * Executes the operation.
     * @param memberId the memberId
     * @return the result of the operation
     */
    int clear(Long memberId);
}
