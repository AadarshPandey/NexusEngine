package com.nexusengine.core.repository;

import com.nexusengine.core.model.SmsFlashPromotionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SmsFlashPromotionLogRepository extends JpaRepository<SmsFlashPromotionLog, Long>, JpaSpecificationExecutor<SmsFlashPromotionLog> {
}
