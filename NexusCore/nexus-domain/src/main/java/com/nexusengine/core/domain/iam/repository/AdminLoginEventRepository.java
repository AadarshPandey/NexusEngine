package com.nexusengine.core.domain.iam.repository;

import com.nexusengine.core.domain.iam.entity.AdminLoginEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminLoginEventRepository extends JpaRepository<AdminLoginEvent, Long>, JpaSpecificationExecutor<AdminLoginEvent> {
    java.util.List<AdminLoginEvent> findTop10ByOrderByCreateTimeDesc();
    java.util.List<AdminLoginEvent> findTop10ByAdminIdOrderByCreateTimeDesc(Long adminId);
}
