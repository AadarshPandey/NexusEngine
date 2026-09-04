package com.nexusengine.core.repository;

import com.nexusengine.core.model.OmsCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OmsCartItemRepository extends JpaRepository<OmsCartItem, Long>, JpaSpecificationExecutor<OmsCartItem> {
    List<OmsCartItem> findByMemberIdAndDeleteStatus(Long memberId, Integer deleteStatus);
    OmsCartItem findByMemberIdAndProductIdAndProductSkuIdAndDeleteStatus(Long memberId, Long productId, Long productSkuId, Integer deleteStatus);
    OmsCartItem findByMemberIdAndProductIdAndDeleteStatus(Long memberId, Long productId, Integer deleteStatus);
}
