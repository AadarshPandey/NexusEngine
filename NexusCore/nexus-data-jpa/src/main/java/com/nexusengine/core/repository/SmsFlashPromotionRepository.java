package com.nexusengine.core.repository;

import com.nexusengine.core.model.SmsFlashPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface SmsFlashPromotionRepository extends JpaRepository<SmsFlashPromotion, Long>, JpaSpecificationExecutor<SmsFlashPromotion> {
    List<SmsFlashPromotion> findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Integer status, Date startDate, Date endDate);
}
