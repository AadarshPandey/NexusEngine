package com.nexusengine.core.domain.ordering.repository;

import com.nexusengine.core.domain.ordering.entity.ReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Long>, JpaSpecificationExecutor<ReturnRequest> {
    java.util.List<ReturnRequest> findByOrderId(Long orderId);
    java.util.List<ReturnRequest> findByOrderIdIn(java.util.List<Long> orderIds);
}
