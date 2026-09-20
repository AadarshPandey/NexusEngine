package com.nexusengine.core.repository;

import com.nexusengine.core.model.SmsCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SmsCouponRepository extends JpaRepository<SmsCoupon, Long>, JpaSpecificationExecutor<SmsCoupon> {

    @Modifying
    @Query("UPDATE SmsCoupon c SET c.count = c.count - 1, c.receiveCount = c.receiveCount + 1 " +
           "WHERE c.id = :couponId AND c.count > 0")
    int decrementCouponStock(@Param("couponId") Long couponId);
}
