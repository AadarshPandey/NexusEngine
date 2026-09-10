package com.nexusengine.core.portal.service;

import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.portal.domain.PmsPortalProductDetail;
import com.nexusengine.core.portal.domain.PmsProductCategoryNode;

import java.util.List;

/**
 * Represents the PmsPortalProductService component.
 * Provides core functionality and operations for PmsPortalProductService.
 */
public interface PmsPortalProductService {
        /**
     * Executes the operation.
     * @param keyword the keyword
     * @param brandId the brandId
     * @param productCategoryId the productCategoryId
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @param sort the sort
     * @return the result of the operation
     */
    List<PmsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort);

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<PmsProductCategoryNode> categoryTreeList();

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    PmsPortalProductDetail detail(Long id);
}
