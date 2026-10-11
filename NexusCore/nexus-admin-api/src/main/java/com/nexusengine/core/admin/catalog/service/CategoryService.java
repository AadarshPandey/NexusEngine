package com.nexusengine.core.admin.catalog.service;

import com.nexusengine.core.domain.catalog.entity.Category;

import com.nexusengine.core.admin.catalog.dto.CategoryParam;
import com.nexusengine.core.admin.catalog.dto.CategoryWithChildrenItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Represents the CategoryService component.
 * Provides core functionality and operations for CategoryService.
 */
public interface CategoryService {
        /**
     * Executes the operation.
     * @param categoryParam the categoryParam
     * @return the result of the operation
     */
    @Transactional
    int create(CategoryParam categoryParam);

        /**
     * Executes the operation.
     * @param id the id
     * @param categoryParam the categoryParam
     * @return the result of the operation
     */
    @Transactional
    int update(Long id, CategoryParam categoryParam);

        /**
     * Executes the operation.
     * @param parentId the parentId
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    org.springframework.data.domain.Page<Category> getList(Long parentId, Integer pageSize, Integer pageNum);

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
    Category getItem(Long id);

        /**
     * Executes the operation.
     * @param ids the ids
     * @param navStatus the navStatus
     * @return the result of the operation
     */
    int updateNavStatus(List<Long> ids, Integer navStatus);

        /**
     * Executes the operation.
     * @param ids the ids
     * @param showStatus the showStatus
     * @return the result of the operation
     */
    int updateShowStatus(List<Long> ids, Integer showStatus);

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<CategoryWithChildrenItem> listWithChildren();
}
