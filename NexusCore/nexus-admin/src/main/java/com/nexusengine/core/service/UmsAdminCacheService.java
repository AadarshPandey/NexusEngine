package com.nexusengine.core.service;

import com.nexusengine.core.model.UmsAdmin;
import com.nexusengine.core.model.UmsResource;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2020/3/13.
 */
public interface UmsAdminCacheService {
    /**
     * Auto-generated documentation
     */
    void delAdmin(Long adminId);

    /**
     * Auto-generated documentation
     */
    void delResourceList(Long adminId);

    /**
     * Auto-generated documentation
     */
    void delResourceListByRole(Long roleId);

    /**
     * Auto-generated documentation
     */
    void delResourceListByRoleIds(List<Long> roleIds);

    /**
     * Auto-generated documentation
     */
    void delResourceListByResource(Long resourceId);

    /**
     * Auto-generated documentation
     */
    UmsAdmin getAdmin(String username);

    /**
     * Auto-generated documentation
     */
    void setAdmin(UmsAdmin admin);

    /**
     * Auto-generated documentation
     */
    List<UmsResource> getResourceList(Long adminId);

    /**
     * Auto-generated documentation
     */
    void setResourceList(Long adminId, List<UmsResource> resourceList);
}
