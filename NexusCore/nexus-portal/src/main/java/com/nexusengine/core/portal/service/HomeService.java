package com.nexusengine.core.portal.service;

import com.nexusengine.core.model.CmsSubject;
import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.PmsProductCategory;
import com.nexusengine.core.portal.domain.HomeContentResult;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2019/1/28.
 */
public interface HomeService {

    /**
     * Auto-generated documentation
     */
    HomeContentResult content();

    /**
     * Auto-generated documentation
     */
    List<PmsProduct> recommendProductList(Integer pageSize, Integer pageNum);

    /**
     * Auto-generated documentation
     * Auto-generated documentation
     */
    List<PmsProductCategory> getProductCateList(Long parentId);

    /**
     * Auto-generated documentation
     * Auto-generated documentation
     */
    List<CmsSubject> getSubjectList(Long cateId, Integer pageSize, Integer pageNum);

    /**
     * Auto-generated documentation
     */
    List<PmsProduct> hotProductList(Integer pageNum, Integer pageSize);

    /**
     * Auto-generated documentation
     */
    List<PmsProduct> newProductList(Integer pageNum, Integer pageSize);
}
