package com.nexusengine.core.service;

import com.nexusengine.core.model.PmsSkuStock;

import java.util.List;

/**
 * Represents the PmsSkuStockService component.
 * Provides core functionality and operations for PmsSkuStockService.
 */
public interface PmsSkuStockService {
        /**
     * Executes the operation.
     * @param pid the pid
     * @param keyword the keyword
     * @return the result of the operation
     */
    List<PmsSkuStock> getList(Long pid, String keyword);

        /**
     * Executes the operation.
     * @param pid the pid
     * @param skuStockList the skuStockList
     * @return the result of the operation
     */
    int update(Long pid, List<PmsSkuStock> skuStockList);
}
