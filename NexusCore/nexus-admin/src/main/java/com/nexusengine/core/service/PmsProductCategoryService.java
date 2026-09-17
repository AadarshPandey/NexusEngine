package com.nexusengine.core.service;

import com.nexusengine.core.dto.PmsProductCategoryParam;
import com.nexusengine.core.dto.PmsProductCategoryWithChildrenItem;
import com.nexusengine.core.model.PmsProductCategory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Represents the PmsProductCategoryService component.
 * Provides core functionality and operations for PmsProductCategoryService.
 */
public interface PmsProductCategoryService {
        /**
     * Executes the operation.
     * @param pmsProductCategoryParam the pmsProductCategoryParam
     * @return the result of the operation
     */
    @Transactional
    int create(PmsProductCategoryParam pmsProductCategoryParam);

        /**
     * Executes the operation.
     * @param id the id
     * @param pmsProductCategoryParam the pmsProductCategoryParam
     * @return the result of the operation
     */
    @Transactional
    int update(Long id, PmsProductCategoryParam pmsProductCategoryParam);

        /**
     * Executes the operation.
     * @param parentId the parentId
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    org.springframework.data.domain.Page<PmsProductCategory> getList(Long parentId, Integer pageSize, Integer pageNum);

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
    PmsProductCategory getItem(Long id);

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
    List<PmsProductCategoryWithChildrenItem> listWithChildren();
}
