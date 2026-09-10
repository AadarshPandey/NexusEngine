package com.nexusengine.core.service;

import com.nexusengine.core.model.UmsResource;

import java.util.List;

/**
 * Represents the UmsResourceService component.
 * Provides core functionality and operations for UmsResourceService.
 */
public interface UmsResourceService {
        /**
     * Executes the operation.
     * @param umsResource the umsResource
     * @return the result of the operation
     */
    int create(UmsResource umsResource);

        /**
     * Executes the operation.
     * @param id the id
     * @param umsResource the umsResource
     * @return the result of the operation
     */
    int update(Long id, UmsResource umsResource);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    UmsResource getItem(Long id);

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
    org.springframework.data.domain.Page<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<UmsResource> listAll();
}
