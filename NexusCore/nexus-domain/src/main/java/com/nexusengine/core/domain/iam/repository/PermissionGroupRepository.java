package com.nexusengine.core.domain.iam.repository;

import com.nexusengine.core.domain.iam.entity.PermissionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PermissionGroupRepository extends JpaRepository<PermissionGroup, Long>, JpaSpecificationExecutor<PermissionGroup> {
}
