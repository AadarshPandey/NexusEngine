package com.nexusengine.core.portal.service;

import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.portal.domain.PmsPortalProductDetail;
import com.nexusengine.core.portal.domain.PmsProductCategoryNode;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2020/4/6.
 */
public interface PmsPortalProductService {
    /**
     * Auto-generated documentation
     */
    List<PmsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort);

    /**
     * Auto-generated documentation
     */
    List<PmsProductCategoryNode> categoryTreeList();

    /**
     * Auto-generated documentation
     */
    PmsPortalProductDetail detail(Long id);
}
