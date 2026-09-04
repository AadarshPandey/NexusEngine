package com.nexusengine.core.repository;
import com.nexusengine.core.model.SmsCouponHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
public interface SmsCouponHistoryRepository extends JpaRepository<SmsCouponHistory, Long>, JpaSpecificationExecutor<SmsCouponHistory> {
    List<SmsCouponHistory> findByMemberId(Long memberId);
    List<SmsCouponHistory> findByMemberIdAndUseStatus(Long memberId, Integer useStatus);
    long countByCouponIdAndMemberId(Long couponId, Long memberId);
    List<SmsCouponHistory> findByCouponId(Long couponId);
    List<SmsCouponHistory> findByCouponIdAndUseStatus(Long couponId, Integer useStatus);
}
