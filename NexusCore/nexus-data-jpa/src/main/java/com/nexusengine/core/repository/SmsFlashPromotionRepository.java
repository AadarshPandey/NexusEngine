package com.nexusengine.core.repository;

import com.nexusengine.core.model.SmsFlashPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SmsFlashPromotionRepository extends JpaRepository<SmsFlashPromotion, Long> {
    List<SmsFlashPromotion> findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Integer status, Date startDate, Date endDate);
}
