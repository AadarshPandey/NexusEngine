package com.nexusengine.core.domain.iam.repository;

import com.nexusengine.core.domain.iam.entity.RolePermission;

import com.nexusengine.core.domain.iam.entity.AdminUserRole;
import com.nexusengine.core.domain.iam.entity.Permission;
import com.nexusengine.core.domain.iam.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminUserRoleRepository extends JpaRepository<AdminUserRole, Long>, JpaSpecificationExecutor<AdminUserRole> {
    
    void deleteByAdminId(Long adminId);

    List<AdminUserRole> findByRoleId(Long roleId);

    List<AdminUserRole> findByRoleIdIn(List<Long> roleIds);

    @Query("SELECT r FROM Role r INNER JOIN AdminUserRole ar ON r.id = ar.roleId WHERE ar.adminId = :adminId")
    List<Role> getRoleList(@Param("adminId") Long adminId);

    @Query("SELECT res FROM Permission res INNER JOIN RolePermission rrr ON res.id = rrr.resourceId INNER JOIN AdminUserRole arr ON rrr.roleId = arr.roleId WHERE arr.adminId = :adminId")
    List<Permission> getResourceList(@Param("adminId") Long adminId);

    @Query("SELECT arr.adminId FROM AdminUserRole arr INNER JOIN RolePermission rrr ON arr.roleId = rrr.roleId WHERE rrr.resourceId = :resourceId")
    List<Long> getAdminIdList(@Param("resourceId") Long resourceId);
}
