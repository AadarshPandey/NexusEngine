package com.nexusengine.core.service;

import com.nexusengine.core.model.SmsHomeRecommendProduct;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/11/7.
 */
public interface SmsHomeRecommendProductService {
    /**
     * Auto-generated documentation
     */
    @Transactional
    int create(List<SmsHomeRecommendProduct> homeRecommendProductList);

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
    List<SmsHomeRecommendProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
