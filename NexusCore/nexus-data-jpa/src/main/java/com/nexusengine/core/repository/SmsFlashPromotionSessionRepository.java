package com.nexusengine.core.repository;

import com.nexusengine.core.model.SmsFlashPromotionSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface SmsFlashPromotionSessionRepository extends JpaRepository<SmsFlashPromotionSession, Long>, JpaSpecificationExecutor<SmsFlashPromotionSession> {
    List<SmsFlashPromotionSession> findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(Date startTime, Date endTime);
    List<SmsFlashPromotionSession> findByStartTimeGreaterThanOrderByStartTimeAsc(Date startTime);
}
