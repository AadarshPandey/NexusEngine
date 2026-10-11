package com.nexusengine.core.domain.iam.repository;


import com.nexusengine.core.domain.iam.entity.ConsoleMenu;
import com.nexusengine.core.domain.iam.entity.Permission;
import com.nexusengine.core.domain.iam.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    @Query("SELECT m FROM ConsoleMenu m INNER JOIN RoleMenu rmr ON m.id = rmr.menuId INNER JOIN AdminUserRole arr ON rmr.roleId = arr.roleId WHERE arr.adminId = :adminId GROUP BY m.id")
    List<ConsoleMenu> getMenuList(@Param("adminId") Long adminId);

    @Query("SELECT m FROM ConsoleMenu m INNER JOIN RoleMenu rmr ON m.id = rmr.menuId WHERE rmr.roleId = :roleId")
    List<ConsoleMenu> getMenuListByRoleId(@Param("roleId") Long roleId);

    @Query("SELECT r FROM Permission r INNER JOIN RolePermission rrr ON r.id = rrr.resourceId WHERE rrr.roleId = :roleId")
    List<Permission> getResourceListByRoleId(@Param("roleId") Long roleId);
}
