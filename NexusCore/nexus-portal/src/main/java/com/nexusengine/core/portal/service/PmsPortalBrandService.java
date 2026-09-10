package com.nexusengine.core.portal.service;

import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.model.PmsBrand;
import com.nexusengine.core.model.PmsProduct;

import java.util.List;

/**
 * Represents the PmsPortalBrandService component.
 * Provides core functionality and operations for PmsPortalBrandService.
 */
public interface PmsPortalBrandService {
        /**
     * Executes the operation.
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    List<PmsBrand> recommendList(Integer pageNum, Integer pageSize);

        /**
     * Executes the operation.
     * @param brandId the brandId
     * @return the result of the operation
     */
    PmsBrand detail(Long brandId);

        /**
     * Executes the operation.
     * @param brandId the brandId
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    CommonPage<PmsProduct> productList(Long brandId, Integer pageNum, Integer pageSize);
}
