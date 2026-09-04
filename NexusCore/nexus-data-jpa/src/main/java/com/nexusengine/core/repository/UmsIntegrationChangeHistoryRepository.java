package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsIntegrationChangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UmsIntegrationChangeHistoryRepository extends JpaRepository<UmsIntegrationChangeHistory, Long>, JpaSpecificationExecutor<UmsIntegrationChangeHistory> {
}
