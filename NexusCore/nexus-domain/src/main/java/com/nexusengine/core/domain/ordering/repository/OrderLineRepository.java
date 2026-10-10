package com.nexusengine.core.domain.ordering.repository;

import com.nexusengine.core.domain.ordering.entity.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long>, JpaSpecificationExecutor<OrderLine> {
    List<OrderLine> findByOrderId(Long orderId);
    List<OrderLine> findByOrderIdIn(java.util.List<Long> orderIds);
}
