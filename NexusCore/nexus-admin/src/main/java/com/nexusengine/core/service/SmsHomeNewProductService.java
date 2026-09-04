package com.nexusengine.core.service;

import com.nexusengine.core.model.SmsHomeNewProduct;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/11/6.
 */
public interface SmsHomeNewProductService {
    /**
     * Auto-generated documentation
     */
    @Transactional
    int create(List<SmsHomeNewProduct> homeNewProductList);

    /**
     * Auto-generated documentation
     */
    int updateSort(Long id, Integer sort);

    /**
     * Auto-generated documentation
     */
    int delete(List<Long> ids);

    /**
     * Auto-generated documentation
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * Auto-generated documentation
     */
    List<SmsHomeNewProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
