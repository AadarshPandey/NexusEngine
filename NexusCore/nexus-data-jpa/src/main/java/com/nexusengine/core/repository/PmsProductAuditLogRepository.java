package com.nexusengine.core.repository;

import com.nexusengine.core.model.PmsProductAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PmsProductAuditLogRepository extends JpaRepository<PmsProductAuditLog, Long>, JpaSpecificationExecutor<PmsProductAuditLog> {
    java.util.List<PmsProductAuditLog> findByProductId(Long productId);
}
