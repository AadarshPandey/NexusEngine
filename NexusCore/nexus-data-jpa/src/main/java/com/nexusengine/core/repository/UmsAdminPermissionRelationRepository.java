package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsAdminPermissionRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UmsAdminPermissionRelationRepository extends JpaRepository<UmsAdminPermissionRelation, Long>, JpaSpecificationExecutor<UmsAdminPermissionRelation> {
}
