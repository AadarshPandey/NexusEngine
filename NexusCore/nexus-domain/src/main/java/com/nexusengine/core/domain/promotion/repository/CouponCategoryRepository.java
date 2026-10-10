package com.nexusengine.core.domain.promotion.repository;
import com.nexusengine.core.domain.promotion.entity.CouponCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CouponCategoryRepository extends JpaRepository<CouponCategory, Long> {
    List<CouponCategory> findByCouponId(Long couponId);
    void deleteByCouponId(Long couponId);
}
