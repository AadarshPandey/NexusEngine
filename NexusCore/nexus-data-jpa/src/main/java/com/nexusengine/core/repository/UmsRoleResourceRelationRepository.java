package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsRoleResourceRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UmsRoleResourceRelationRepository extends JpaRepository<UmsRoleResourceRelation, Long>, JpaSpecificationExecutor<UmsRoleResourceRelation> {
    void deleteByRoleId(Long roleId);
}
