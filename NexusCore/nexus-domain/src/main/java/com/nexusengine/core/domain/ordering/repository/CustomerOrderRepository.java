package com.nexusengine.core.domain.ordering.repository;

import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long>, JpaSpecificationExecutor<CustomerOrder> {
    CustomerOrder findByOrderSn(String orderSn);
    Page<CustomerOrder> findByMemberIdAndDeleteStatusOrderByCreateTimeDesc(Long memberId, Integer deleteStatus, Pageable pageable);
    Page<CustomerOrder> findByMemberIdAndStatusAndDeleteStatusOrderByCreateTimeDesc(Long memberId, Integer status, Integer deleteStatus, Pageable pageable);
    List<CustomerOrder> findByIdAndStatusAndDeleteStatus(Long id, Integer status, Integer deleteStatus);
    List<CustomerOrder> findByStatusAndDeleteStatusAndCreateTimeBefore(Integer status, Integer deleteStatus, java.time.LocalDateTime deadline);
}
