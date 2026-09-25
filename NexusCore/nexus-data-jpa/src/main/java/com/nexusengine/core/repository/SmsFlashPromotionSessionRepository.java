package com.nexusengine.core.repository;

import com.nexusengine.core.model.SmsFlashPromotionSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface SmsFlashPromotionSessionRepository extends JpaRepository<SmsFlashPromotionSession, Long> {
    List<SmsFlashPromotionSession> findByStatus(Integer status);
    
    Optional<SmsFlashPromotionSession> findFirstByStatusAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(Integer status, LocalTime startTime, LocalTime endTime);
}
