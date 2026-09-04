package com.nexusengine.core.repository;

import com.nexusengine.core.model.OmsOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OmsOrderItemRepository extends JpaRepository<OmsOrderItem, Long>, JpaSpecificationExecutor<OmsOrderItem> {
    List<OmsOrderItem> findByOrderId(Long orderId);
}
