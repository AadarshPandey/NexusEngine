package com.nexusengine.core.domain.ordering.repository;

import com.nexusengine.core.domain.ordering.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long>, JpaSpecificationExecutor<CartItem> {
    List<CartItem> findByMemberId(Long memberId);
    CartItem findByMemberIdAndProductIdAndProductSkuId(Long memberId, Long productId, Long productSkuId);
    CartItem findByMemberIdAndProductId(Long memberId, Long productId);
}
