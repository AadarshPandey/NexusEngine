package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsAdminRoleRelation;
import com.nexusengine.core.model.UmsResource;
import com.nexusengine.core.model.UmsRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UmsAdminRoleRelationRepository extends JpaRepository<UmsAdminRoleRelation, Long>, JpaSpecificationExecutor<UmsAdminRoleRelation> {
    
    void deleteByAdminId(Long adminId);

    List<UmsAdminRoleRelation> findByRoleId(Long roleId);

    List<UmsAdminRoleRelation> findByRoleIdIn(List<Long> roleIds);

    @Query("SELECT r FROM UmsRole r INNER JOIN UmsAdminRoleRelation ar ON r.id = ar.roleId WHERE ar.adminId = :adminId")
    List<UmsRole> getRoleList(@Param("adminId") Long adminId);

    @Query("SELECT res FROM UmsResource res INNER JOIN UmsRoleResourceRelation rrr ON res.id = rrr.resourceId INNER JOIN UmsAdminRoleRelation arr ON rrr.roleId = arr.roleId WHERE arr.adminId = :adminId")
    List<UmsResource> getResourceList(@Param("adminId") Long adminId);

    @Query("SELECT arr.adminId FROM UmsAdminRoleRelation arr INNER JOIN UmsRoleResourceRelation rrr ON arr.roleId = rrr.roleId WHERE rrr.resourceId = :resourceId")
    List<Long> getAdminIdList(@Param("resourceId") Long resourceId);
}
