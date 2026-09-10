package com.nexusengine.core.service;

import com.nexusengine.core.model.UmsResourceCategory;

import java.util.List;

/**
 * Represents the UmsResourceCategoryService component.
 * Provides core functionality and operations for UmsResourceCategoryService.
 */
public interface UmsResourceCategoryService {

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<UmsResourceCategory> listAll();

        /**
     * Executes the operation.
     * @param umsResourceCategory the umsResourceCategory
     * @return the result of the operation
     */
    int create(UmsResourceCategory umsResourceCategory);

        /**
     * Executes the operation.
     * @param id the id
     * @param umsResourceCategory the umsResourceCategory
     * @return the result of the operation
     */
    int update(Long id, UmsResourceCategory umsResourceCategory);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    int delete(Long id);
}
