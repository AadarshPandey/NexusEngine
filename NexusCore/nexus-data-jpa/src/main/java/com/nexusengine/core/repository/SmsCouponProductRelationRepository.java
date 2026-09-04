package com.nexusengine.core.repository;
import com.nexusengine.core.model.SmsCouponProductRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SmsCouponProductRelationRepository extends JpaRepository<SmsCouponProductRelation, Long> {
    List<SmsCouponProductRelation> findByCouponId(Long couponId);
    void deleteByCouponId(Long couponId);
}
