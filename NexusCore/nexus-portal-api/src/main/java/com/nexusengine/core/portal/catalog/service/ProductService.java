package com.nexusengine.core.portal.catalog.service;

import com.nexusengine.core.domain.catalog.entity.Product;


import java.util.List;
import com.nexusengine.core.portal.catalog.dto.CategoryNode;
import com.nexusengine.core.portal.catalog.dto.ProductDetail;


/**
 * Represents the ProductService component.
 * Provides core functionality and operations for ProductService.
 */
public interface ProductService {
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
    List<Product> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort);

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<CategoryNode> categoryTreeList();

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    ProductDetail detail(Long id);
}
