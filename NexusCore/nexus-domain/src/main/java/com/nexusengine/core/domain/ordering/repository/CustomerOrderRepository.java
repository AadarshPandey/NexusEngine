package com.nexusengine.core.domain.ordering.repository;

import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerOrderRepository
        extends JpaRepository<CustomerOrder, Long>, JpaSpecificationExecutor<CustomerOrder> {
    CustomerOrder findByOrderSn(String orderSn);

    Page<CustomerOrder> findByMemberIdAndDeletedAtIsNullOrderByCreateTimeDesc(Long memberId, Pageable pageable);

    Page<CustomerOrder> findByMemberIdAndStatusAndDeletedAtIsNullOrderByCreateTimeDesc(
            Long memberId, Integer status, Pageable pageable);

    List<CustomerOrder> findByIdAndStatusAndDeletedAtIsNull(Long id, Integer status);

    List<CustomerOrder> findByStatusAndDeletedAtIsNullAndCreateTimeBefore(Integer status, OffsetDateTime deadline);

    List<CustomerOrder> findByStatusAndDeletedAtIsNullAndDeliveryTimeBefore(Integer status, OffsetDateTime deadline);
}
