package com.nexusengine.core.repository;

import com.nexusengine.core.model.SmsCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SmsCouponRepository extends JpaRepository<SmsCoupon, Long>, JpaSpecificationExecutor<SmsCoupon> {
}
