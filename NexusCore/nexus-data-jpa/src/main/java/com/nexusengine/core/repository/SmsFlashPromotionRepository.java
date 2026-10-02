package com.nexusengine.core.repository;

import com.nexusengine.core.model.SmsFlashPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SmsFlashPromotionRepository extends JpaRepository<SmsFlashPromotion, Long> {
    org.springframework.data.domain.Page<SmsFlashPromotion> findByTitleContaining(String title, org.springframework.data.domain.Pageable pageable);
    List<SmsFlashPromotion> findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Integer status, java.time.LocalDate startDate, java.time.LocalDate endDate);
}
