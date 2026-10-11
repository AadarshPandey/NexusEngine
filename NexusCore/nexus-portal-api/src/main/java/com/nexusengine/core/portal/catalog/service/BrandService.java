package com.nexusengine.core.portal.catalog.service;

import com.nexusengine.core.common.api.CommonPage;

import com.nexusengine.core.domain.catalog.entity.Brand;
import com.nexusengine.core.domain.catalog.entity.Product;

import java.util.List;


/**
 * Represents the BrandService component.
 * Provides core functionality and operations for BrandService.
 */
public interface BrandService {
        /**
     * Executes the operation.
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    List<Brand> recommendList(Integer pageNum, Integer pageSize);

        /**
     * Executes the operation.
     * @param brandId the brandId
     * @return the result of the operation
     */
    Brand detail(Long brandId);

        /**
     * Executes the operation.
     * @param brandId the brandId
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    CommonPage<Product> productList(Long brandId, Integer pageNum, Integer pageSize);
}
