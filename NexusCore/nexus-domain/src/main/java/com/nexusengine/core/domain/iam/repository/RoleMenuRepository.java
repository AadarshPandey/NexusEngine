package com.nexusengine.core.domain.iam.repository;

import com.nexusengine.core.domain.iam.entity.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long>, JpaSpecificationExecutor<RoleMenu> {
    void deleteByRoleId(Long roleId);
}
