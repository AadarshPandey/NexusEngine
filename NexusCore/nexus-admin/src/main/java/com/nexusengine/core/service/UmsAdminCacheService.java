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
     * Executes the operation.
     * @param adminId the adminId
     */
    void delAdmin(Long adminId);

        /**
     * Executes the operation.
     * @param adminId the adminId
     */
    void delResourceList(Long adminId);

        /**
     * Executes the operation.
     * @param roleId the roleId
     */
    void delResourceListByRole(Long roleId);

        /**
     * Executes the operation.
     * @param roleIds the roleIds
     */
    void delResourceListByRoleIds(List<Long> roleIds);

        /**
     * Executes the operation.
     * @param resourceId the resourceId
     */
    void delResourceListByResource(Long resourceId);

        /**
     * Executes the operation.
     * @param username the username
     * @return the result of the operation
     */
    UmsAdmin getAdmin(String username);

        /**
     * Executes the operation.
     * @param admin the admin
     */
    void setAdmin(UmsAdmin admin);

        /**
     * Executes the operation.
     * @param adminId the adminId
     * @return the result of the operation
     */
    List<UmsResource> getResourceList(Long adminId);

        /**
     * Executes the operation.
     * @param adminId the adminId
     * @param resourceList the resourceList
     */
    void setResourceList(Long adminId, List<UmsResource> resourceList);
}
