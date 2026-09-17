package com.nexusengine.core.service;

import com.nexusengine.core.dto.PmsProductAttributeCategoryItem;
import com.nexusengine.core.model.PmsProductAttributeCategory;

import java.util.List;

/**
 * Represents the PmsProductAttributeCategoryService component.
 * Provides core functionality and operations for PmsProductAttributeCategoryService.
 */
public interface PmsProductAttributeCategoryService {
        /**
     * Executes the operation.
     * @param name the name
     * @return the result of the operation
     */
    int create(String name);

        /**
     * Executes the operation.
     * @param id the id
     * @param name the name
     * @return the result of the operation
     */
    int update(Long id, String name);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    int delete(Long id);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    PmsProductAttributeCategory getItem(Long id);

        /**
     * Executes the operation.
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    org.springframework.data.domain.Page<PmsProductAttributeCategory> getList(Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<PmsProductAttributeCategoryItem> getListWithAttr();
}
