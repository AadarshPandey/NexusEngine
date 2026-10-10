package com.nexusengine.core.domain.promotion.repository;
import com.nexusengine.core.domain.promotion.entity.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long>, JpaSpecificationExecutor<MemberCoupon> {
    List<MemberCoupon> findByMemberId(Long memberId);
    List<MemberCoupon> findByMemberIdAndUseStatus(Long memberId, Integer useStatus);
    long countByCouponIdAndMemberId(Long couponId, Long memberId);
    List<MemberCoupon> findByCouponId(Long couponId);
    List<MemberCoupon> findByCouponIdAndUseStatus(Long couponId, Integer useStatus);
}
