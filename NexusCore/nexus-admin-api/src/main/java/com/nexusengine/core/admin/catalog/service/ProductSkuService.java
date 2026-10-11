package com.nexusengine.core.admin.catalog.service;

import com.nexusengine.core.domain.catalog.entity.ProductSku;



import java.util.List;


/**
 * Represents the ProductSkuService component.
 * Provides core functionality and operations for ProductSkuService.
 */
public interface ProductSkuService {
        /**
     * Executes the operation.
     * @param pid the pid
     * @param keyword the keyword
     * @return the result of the operation
     */
    List<ProductSku> getList(Long pid, String keyword);

        /**
     * Executes the operation.
     * @param pid the pid
     * @param skuStockList the skuStockList
     * @return the result of the operation
     */
    int update(Long pid, List<ProductSku> skuStockList);
}
