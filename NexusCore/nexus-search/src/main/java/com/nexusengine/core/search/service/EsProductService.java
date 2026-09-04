package com.nexusengine.core.search.service;

import com.nexusengine.core.search.domain.EsProduct;
import com.nexusengine.core.search.domain.EsProductRelatedInfo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/6/19.
 */
public interface EsProductService {
    /**
     * Auto-generated documentation
     */
    int importAll();

    /**
     * Auto-generated documentation
     */
    void delete(Long id);

    /**
     * Auto-generated documentation
     */
    EsProduct create(Long id);

    /**
     * Auto-generated documentation
     */
    void delete(List<Long> ids);

    /**
     * Auto-generated documentation
     */
    Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize);

    /**
     * Auto-generated documentation
     */
    Page<EsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize,Integer sort);

    /**
     * Auto-generated documentation
     */
    Page<EsProduct> recommend(Long id, Integer pageNum, Integer pageSize);

    /**
     * Auto-generated documentation
     */
    EsProductRelatedInfo searchRelatedInfo(String keyword);
}
