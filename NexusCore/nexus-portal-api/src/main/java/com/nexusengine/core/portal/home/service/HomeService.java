package com.nexusengine.core.portal.home.service;

import com.nexusengine.core.domain.catalog.entity.Product;

import com.nexusengine.core.domain.catalog.entity.Category;

import java.util.List;
import com.nexusengine.core.portal.home.dto.HomeContentResult;


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
    List<Product> recommendProductList(Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @param parentId the parentId
     * @return the result of the operation
     */
    List<Category> getProductCateList(Long parentId);

        /**
     * Executes the operation.
     * @param cateId the cateId
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */

        /**
     * Executes the operation.
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    List<Product> hotProductList(Integer pageNum, Integer pageSize);

        /**
     * Executes the operation.
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    List<Product> newProductList(Integer pageNum, Integer pageSize);

    /**
     * Records a banner click event and increments its click counter.
     * @param id the banner id
     */
    void recordBannerClick(Long id);
}
