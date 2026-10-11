package com.nexusengine.core.portal.ordering.service;

import lombok.RequiredArgsConstructor;

import cn.hutool.core.collection.CollUtil;

import com.nexusengine.core.domain.ordering.entity.CartItem;
import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.domain.ordering.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.nexusengine.core.portal.ordering.dto.CartProduct;
import com.nexusengine.core.portal.ordering.dto.CartPromotionItem;
import com.nexusengine.core.portal.customer.service.MemberService;
import com.nexusengine.core.portal.catalog.dao.ProductDao;


/**
 * Shopping cart management Service implementation
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductDao productDao;
    private final PromotionCalculationService promotionService;
    private final MemberService memberService;

    @Override
    public int add(CartItem cartItem) {
        Member currentMember = memberService.getCurrentMember();
        cartItem.setMemberId(currentMember.getId());
        cartItem.setDeleteStatus(0);
        CartItem existCartItem = getCartItem(cartItem);
        if (existCartItem == null) {
            cartItem.setCreateDate(java.time.OffsetDateTime.now());
            cartItemRepository.save(cartItem);
        } else {
            existCartItem.setModifyDate(java.time.OffsetDateTime.now());
            existCartItem.setQuantity(existCartItem.getQuantity() + cartItem.getQuantity());
            cartItemRepository.save(existCartItem);
        }
        return 1;
    }

    private CartItem getCartItem(CartItem cartItem) {
        if (cartItem.getProductSkuId() != null) {
            return cartItemRepository.findByMemberIdAndProductIdAndProductSkuId(
                    cartItem.getMemberId(), cartItem.getProductId(), cartItem.getProductSkuId());
        }
        return cartItemRepository.findByMemberIdAndProductId(
                cartItem.getMemberId(), cartItem.getProductId());
    }

    @Override
    public List<CartItem> list(Long memberId) {
        return cartItemRepository.findByMemberId(memberId);
    }

    @Override
    public List<CartPromotionItem> listPromotion(Long memberId, List<Long> cartIds) {
        List<CartItem> cartItemList = list(memberId);
        if (CollUtil.isNotEmpty(cartIds)) {
            cartItemList = cartItemList.stream()
                    .filter(item -> cartIds.contains(item.getId()))
                    .collect(Collectors.toList());
        }
        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cartItemList)) {
            cartPromotionItemList = promotionService.calcCartPromotion(cartItemList);
        }
        return cartPromotionItemList;
    }

    @Override
    public int updateQuantity(Long id, Long memberId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(id).orElse(null);
        if (cartItem != null && cartItem.getMemberId().equals(memberId)) {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
            return 1;
        }
        return 0;
    }

    @Override
    public int delete(Long memberId, List<Long> ids) {
        List<CartItem> items = cartItemRepository.findAllById(ids);
        for (CartItem item : items) {
            if (item.getMemberId().equals(memberId)) {
                cartItemRepository.delete(item);
                
            }
        }
        return ids.size();
    }

    @Override
    public CartProduct getCartProduct(Long productId) {
        return productDao.getCartProduct(productId);
    }

    @Override
    public int updateAttr(CartItem cartItem) {
        CartItem updateCart = cartItemRepository.findById(cartItem.getId()).orElse(null);
        Member currentMember = memberService.getCurrentMember();
        if (updateCart != null && updateCart.getMemberId().equals(currentMember.getId())) {
            updateCart.setModifyDate(java.time.OffsetDateTime.now());
            cartItemRepository.delete(updateCart);
            
        }
        cartItem.setId(null);
        add(cartItem);
        return 1;
    }

    @Override
    public int clear(Long memberId) {
        List<CartItem> items = cartItemRepository.findByMemberId(memberId);
        for (CartItem item : items) {
            cartItemRepository.delete(item);
        }
        cartItemRepository.deleteAll(items);
        return items.size();
    }
}
