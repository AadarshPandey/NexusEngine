package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UmsPermissionRepository extends JpaRepository<UmsPermission, Long>, JpaSpecificationExecutor<UmsPermission> {
}
