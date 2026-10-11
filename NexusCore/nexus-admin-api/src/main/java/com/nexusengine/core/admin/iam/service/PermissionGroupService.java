package com.nexusengine.core.admin.iam.service;

import com.nexusengine.core.domain.iam.entity.PermissionGroup;



import java.util.List;


/**
 * Represents the PermissionGroupService component.
 * Provides core functionality and operations for PermissionGroupService.
 */
public interface PermissionGroupService {

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<PermissionGroup> listAll();

        /**
     * Executes the operation.
     * @param permissionGroup the permissionGroup
     * @return the result of the operation
     */
    int create(PermissionGroup permissionGroup);

        /**
     * Executes the operation.
     * @param id the id
     * @param permissionGroup the permissionGroup
     * @return the result of the operation
     */
    int update(Long id, PermissionGroup permissionGroup);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    int delete(Long id);
}
