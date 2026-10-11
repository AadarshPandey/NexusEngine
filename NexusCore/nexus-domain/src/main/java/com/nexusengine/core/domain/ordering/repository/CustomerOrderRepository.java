package com.nexusengine.core.domain.ordering.repository;

import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long>, JpaSpecificationExecutor<CustomerOrder> {
    CustomerOrder findByOrderSn(String orderSn);
    Page<CustomerOrder> findByMemberIdAndDeleteStatusIsNullOrderByCreateTimeDesc(Long memberId, Pageable pageable);
    Page<CustomerOrder> findByMemberIdAndStatusAndDeleteStatusIsNullOrderByCreateTimeDesc(Long memberId, Integer status, Pageable pageable);
    List<CustomerOrder> findByIdAndStatusAndDeleteStatusIsNull(Long id, Integer status);
    List<CustomerOrder> findByStatusAndDeleteStatusIsNullAndCreateTimeBefore(Integer status, java.time.LocalDateTime deadline);
    List<CustomerOrder> findByStatusAndDeleteStatusIsNullAndDeliveryTimeBefore(Integer status, java.time.OffsetDateTime deadline);
}
