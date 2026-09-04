package com.nexusengine.core.portal.service;

import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.model.PmsBrand;
import com.nexusengine.core.model.PmsProduct;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2020/5/15.
 */
public interface PmsPortalBrandService {
    /**
     * Auto-generated documentation
     */
    List<PmsBrand> recommendList(Integer pageNum, Integer pageSize);

    /**
     * Auto-generated documentation
     */
    PmsBrand detail(Long brandId);

    /**
     * Auto-generated documentation
     */
    CommonPage<PmsProduct> productList(Long brandId, Integer pageNum, Integer pageSize);
}
