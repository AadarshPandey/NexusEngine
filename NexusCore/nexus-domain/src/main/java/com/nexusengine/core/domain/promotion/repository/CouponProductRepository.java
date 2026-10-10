package com.nexusengine.core.domain.promotion.repository;
import com.nexusengine.core.domain.promotion.entity.CouponProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CouponProductRepository extends JpaRepository<CouponProduct, Long> {
    List<CouponProduct> findByCouponId(Long couponId);
    void deleteByCouponId(Long couponId);
}
