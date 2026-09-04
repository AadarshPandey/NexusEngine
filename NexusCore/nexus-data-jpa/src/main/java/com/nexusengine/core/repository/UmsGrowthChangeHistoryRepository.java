package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsGrowthChangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UmsGrowthChangeHistoryRepository extends JpaRepository<UmsGrowthChangeHistory, Long>, JpaSpecificationExecutor<UmsGrowthChangeHistory> {
}
