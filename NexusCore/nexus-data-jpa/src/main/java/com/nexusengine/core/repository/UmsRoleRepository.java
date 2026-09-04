package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsMenu;
import com.nexusengine.core.model.UmsResource;
import com.nexusengine.core.model.UmsRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UmsRoleRepository extends JpaRepository<UmsRole, Long>, JpaSpecificationExecutor<UmsRole> {
    
    @Query("SELECT m FROM UmsMenu m INNER JOIN UmsRoleMenuRelation rmr ON m.id = rmr.menuId INNER JOIN UmsAdminRoleRelation arr ON rmr.roleId = arr.roleId WHERE arr.adminId = :adminId GROUP BY m.id")
    List<UmsMenu> getMenuList(@Param("adminId") Long adminId);

    @Query("SELECT m FROM UmsMenu m INNER JOIN UmsRoleMenuRelation rmr ON m.id = rmr.menuId WHERE rmr.roleId = :roleId")
    List<UmsMenu> getMenuListByRoleId(@Param("roleId") Long roleId);

    @Query("SELECT r FROM UmsResource r INNER JOIN UmsRoleResourceRelation rrr ON r.id = rrr.resourceId WHERE rrr.roleId = :roleId")
    List<UmsResource> getResourceListByRoleId(@Param("roleId") Long roleId);
}
