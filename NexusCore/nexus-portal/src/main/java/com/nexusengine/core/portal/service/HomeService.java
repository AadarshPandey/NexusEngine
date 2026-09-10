package com.nexusengine.core.portal.service;

import com.nexusengine.core.model.CmsSubject;
import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.PmsProductCategory;
import com.nexusengine.core.portal.domain.HomeContentResult;

import java.util.List;

/**
 * Represents the HomeService component.
 * Provides core functionality and operations for HomeService.
 */
public interface HomeService {

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    HomeContentResult content();

        /**
     * Executes the operation.
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    List<PmsProduct> recommendProductList(Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @param parentId the parentId
     * @return the result of the operation
     */
    List<PmsProductCategory> getProductCateList(Long parentId);

        /**
     * Executes the operation.
     * @param cateId the cateId
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    List<CmsSubject> getSubjectList(Long cateId, Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    List<PmsProduct> hotProductList(Integer pageNum, Integer pageSize);

        /**
     * Executes the operation.
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    List<PmsProduct> newProductList(Integer pageNum, Integer pageSize);
}
