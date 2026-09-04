package com.nexusengine.core.portal.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.nexusengine.core.model.OmsCartItem;
import com.nexusengine.core.model.UmsMember;
import com.nexusengine.core.portal.dao.PortalProductDao;
import com.nexusengine.core.portal.domain.CartProduct;
import com.nexusengine.core.portal.domain.CartPromotionItem;
import com.nexusengine.core.portal.service.OmsCartItemService;
import com.nexusengine.core.portal.service.OmsPromotionService;
import com.nexusengine.core.portal.service.UmsMemberService;
import com.nexusengine.core.repository.OmsCartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Shopping cart management Service implementation
 */
@Service
public class OmsCartItemServiceImpl implements OmsCartItemService {
    @Autowired
    private OmsCartItemRepository cartItemRepository;
    @Autowired
    private PortalProductDao portalProductDao;
    @Autowired
    private OmsPromotionService promotionService;
    @Autowired
    private UmsMemberService memberService;

    @Override
    public int add(OmsCartItem cartItem) {
        UmsMember currentMember = memberService.getCurrentMember();
        cartItem.setMemberId(currentMember.getId());
        cartItem.setMemberNickname(currentMember.getNickname());
        cartItem.setDeleteStatus(0);
        OmsCartItem existCartItem = getCartItem(cartItem);
        if (existCartItem == null) {
            cartItem.setCreateDate(new Date());
            cartItemRepository.save(cartItem);
        } else {
            existCartItem.setModifyDate(new Date());
            existCartItem.setQuantity(existCartItem.getQuantity() + cartItem.getQuantity());
            cartItemRepository.save(existCartItem);
        }
        return 1;
    }

    private OmsCartItem getCartItem(OmsCartItem cartItem) {
        if (cartItem.getProductSkuId() != null) {
            return cartItemRepository.findByMemberIdAndProductIdAndProductSkuIdAndDeleteStatus(
                    cartItem.getMemberId(), cartItem.getProductId(), cartItem.getProductSkuId(), 0);
        }
        return cartItemRepository.findByMemberIdAndProductIdAndDeleteStatus(
                cartItem.getMemberId(), cartItem.getProductId(), 0);
    }

    @Override
    public List<OmsCartItem> list(Long memberId) {
        return cartItemRepository.findByMemberIdAndDeleteStatus(memberId, 0);
    }

    @Override
    public List<CartPromotionItem> listPromotion(Long memberId, List<Long> cartIds) {
        List<OmsCartItem> cartItemList = list(memberId);
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
        OmsCartItem cartItem = cartItemRepository.findById(id).orElse(null);
        if (cartItem != null && cartItem.getMemberId().equals(memberId)) {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
            return 1;
        }
        return 0;
    }

    @Override
    public int delete(Long memberId, List<Long> ids) {
        List<OmsCartItem> items = cartItemRepository.findAllById(ids);
        for (OmsCartItem item : items) {
            if (item.getMemberId().equals(memberId)) {
                item.setDeleteStatus(1);
                cartItemRepository.save(item);
            }
        }
        return ids.size();
    }

    @Override
    public CartProduct getCartProduct(Long productId) {
        return portalProductDao.getCartProduct(productId);
    }

    @Override
    public int updateAttr(OmsCartItem cartItem) {
        OmsCartItem updateCart = cartItemRepository.findById(cartItem.getId()).orElse(null);
        if (updateCart != null) {
            updateCart.setModifyDate(new Date());
            updateCart.setDeleteStatus(1);
            cartItemRepository.save(updateCart);
        }
        cartItem.setId(null);
        add(cartItem);
        return 1;
    }

    @Override
    public int clear(Long memberId) {
        List<OmsCartItem> items = cartItemRepository.findByMemberIdAndDeleteStatus(memberId, 0);
        for (OmsCartItem item : items) {
            item.setDeleteStatus(1);
        }
        cartItemRepository.saveAll(items);
        return items.size();
    }
}
