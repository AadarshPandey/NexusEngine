package com.nexusengine.core.service;

import com.nexusengine.core.model.SmsHomeBrand;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/11/6.
 */
public interface SmsHomeBrandService {
    /**
     * Auto-generated documentation
     */
    @Transactional
    int create(List<SmsHomeBrand> homeBrandList);

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
    List<SmsHomeBrand> list(String brandName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
