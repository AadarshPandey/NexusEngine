package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsAdminLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UmsAdminLoginLogRepository extends JpaRepository<UmsAdminLoginLog, Long>, JpaSpecificationExecutor<UmsAdminLoginLog> {
}
