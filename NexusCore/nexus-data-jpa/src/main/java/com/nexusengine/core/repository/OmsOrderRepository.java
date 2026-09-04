package com.nexusengine.core.repository;

import com.nexusengine.core.model.OmsOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OmsOrderRepository extends JpaRepository<OmsOrder, Long>, JpaSpecificationExecutor<OmsOrder> {
    List<OmsOrder> findByVendorId(Long vendorId);
    OmsOrder findByOrderSn(String orderSn);
    Page<OmsOrder> findByMemberIdAndDeleteStatusOrderByCreateTimeDesc(Long memberId, Integer deleteStatus, Pageable pageable);
    Page<OmsOrder> findByMemberIdAndStatusAndDeleteStatusOrderByCreateTimeDesc(Long memberId, Integer status, Integer deleteStatus, Pageable pageable);
    List<OmsOrder> findByIdAndStatusAndDeleteStatus(Long id, Integer status, Integer deleteStatus);
}
