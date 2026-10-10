package com.nexusengine.core.portal.ordering.service;

import com.nexusengine.core.domain.ordering.entity.CartItem;
import com.nexusengine.core.portal.catalog.dto.*;
import com.nexusengine.core.portal.catalog.service.*;
import com.nexusengine.core.portal.catalog.dao.*;
import com.nexusengine.core.portal.payment.service.*;
import com.nexusengine.core.portal.ordering.dto.*;
import com.nexusengine.core.portal.ordering.service.*;
import com.nexusengine.core.portal.ordering.dao.*;
import com.nexusengine.core.portal.customer.dto.*;
import com.nexusengine.core.portal.customer.service.*;
import com.nexusengine.core.portal.promotion.dto.*;
import com.nexusengine.core.portal.promotion.service.*;
import com.nexusengine.core.portal.promotion.strategy.*;
import com.nexusengine.core.portal.ai.service.*;
import com.nexusengine.core.portal.home.dto.*;
import com.nexusengine.core.portal.home.service.*;
import com.nexusengine.core.portal.engagement.dto.*;
import com.nexusengine.core.portal.engagement.service.*;

import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;

import java.util.List;


/**
 * Represents the CartService component.
 * Provides core functionality and operations for CartService.
 */
public interface CartService {
        /**
     * Executes the operation.
     * @param cartItem the cartItem
     * @return the result of the operation
     */
    int add(CartItem cartItem);

        /**
     * Executes the operation.
     * @param memberId the memberId
     * @return the result of the operation
     */
    List<CartItem> list(Long memberId);

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
    int updateAttr(CartItem cartItem);

        /**
     * Executes the operation.
     * @param memberId the memberId
     * @return the result of the operation
     */
    int clear(Long memberId);
}
