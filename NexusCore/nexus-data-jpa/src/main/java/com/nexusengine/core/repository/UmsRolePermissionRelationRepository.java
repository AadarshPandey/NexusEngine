package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsRolePermissionRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UmsRolePermissionRelationRepository extends JpaRepository<UmsRolePermissionRelation, Long>, JpaSpecificationExecutor<UmsRolePermissionRelation> {
}
