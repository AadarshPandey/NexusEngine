package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsMemberLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UmsMemberLoginLogRepository extends JpaRepository<UmsMemberLoginLog, Long>, JpaSpecificationExecutor<UmsMemberLoginLog> {
}
