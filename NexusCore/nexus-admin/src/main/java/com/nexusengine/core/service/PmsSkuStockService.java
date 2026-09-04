package com.nexusengine.core.service;

import com.nexusengine.core.model.PmsSkuStock;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/27.
 */
public interface PmsSkuStockService {
    /**
     * Auto-generated documentation
     */
    List<PmsSkuStock> getList(Long pid, String keyword);

    /**
     * Auto-generated documentation
     */
    int update(Long pid, List<PmsSkuStock> skuStockList);
}
