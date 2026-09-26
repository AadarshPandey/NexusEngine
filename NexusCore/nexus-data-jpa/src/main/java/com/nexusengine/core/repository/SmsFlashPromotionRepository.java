package com.nexusengine.core.repository;

import com.nexusengine.core.model.SmsFlashPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SmsFlashPromotionRepository extends JpaRepository<SmsFlashPromotion, Long> {
    List<SmsFlashPromotion> findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Integer status, LocalDateTime startDate, LocalDateTime endDate);
}
