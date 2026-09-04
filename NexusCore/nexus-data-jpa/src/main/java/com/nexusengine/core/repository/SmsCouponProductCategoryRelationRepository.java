package com.nexusengine.core.repository;
import com.nexusengine.core.model.SmsCouponProductCategoryRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SmsCouponProductCategoryRelationRepository extends JpaRepository<SmsCouponProductCategoryRelation, Long> {
    List<SmsCouponProductCategoryRelation> findByCouponId(Long couponId);
    void deleteByCouponId(Long couponId);
}
