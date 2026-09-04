package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsRoleMenuRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UmsRoleMenuRelationRepository extends JpaRepository<UmsRoleMenuRelation, Long>, JpaSpecificationExecutor<UmsRoleMenuRelation> {
    void deleteByRoleId(Long roleId);
}
