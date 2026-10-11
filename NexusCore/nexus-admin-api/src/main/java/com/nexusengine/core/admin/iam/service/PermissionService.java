package com.nexusengine.core.admin.iam.service;

import com.nexusengine.core.domain.iam.entity.Permission;



import java.util.List;


/**
 * Represents the PermissionService component.
 * Provides core functionality and operations for PermissionService.
 */
public interface PermissionService {
        /**
     * Executes the operation.
     * @param permission the permission
     * @return the result of the operation
     */
    int create(Permission permission);

        /**
     * Executes the operation.
     * @param id the id
     * @param permission the permission
     * @return the result of the operation
     */
    int update(Long id, Permission permission);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    Permission getItem(Long id);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    int delete(Long id);

        /**
     * Executes the operation.
     * @param categoryId the categoryId
     * @param nameKeyword the nameKeyword
     * @param urlKeyword the urlKeyword
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    org.springframework.data.domain.Page<Permission> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<Permission> listAll();
}
