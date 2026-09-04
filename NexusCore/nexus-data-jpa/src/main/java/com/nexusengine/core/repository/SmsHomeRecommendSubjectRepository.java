package com.nexusengine.core.repository;

import com.nexusengine.core.model.SmsHomeRecommendSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SmsHomeRecommendSubjectRepository extends JpaRepository<SmsHomeRecommendSubject, Long>, JpaSpecificationExecutor<SmsHomeRecommendSubject> {
}
