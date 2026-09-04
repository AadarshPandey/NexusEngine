package com.nexusengine.core.service;

import com.nexusengine.core.dto.PmsProductParam;
import com.nexusengine.core.dto.PmsProductQueryParam;
import com.nexusengine.core.dto.PmsProductResult;
import com.nexusengine.core.model.PmsProduct;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/26.
 */
public interface PmsProductService {
    /**
     * Auto-generated documentation
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED)
    int create(PmsProductParam productParam);

    /**
     * Auto-generated documentation
     */
    PmsProductResult getUpdateInfo(Long id);

    /**
     * Auto-generated documentation
     */
    @Transactional
    int update(Long id, PmsProductParam productParam);

    /**
     * Auto-generated documentation
     */
    List<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum);

    /**
     * Auto-generated documentation
     * Auto-generated documentation
     * Auto-generated documentation
     * Auto-generated documentation
     */
    @Transactional
    int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail);

    /**
     * Auto-generated documentation
     */
    int updatePublishStatus(List<Long> ids, Integer publishStatus);

    /**
     * Auto-generated documentation
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * Auto-generated documentation
     */
    int updateNewStatus(List<Long> ids, Integer newStatus);

    /**
     * Auto-generated documentation
     */
    int updateDeleteStatus(List<Long> ids, Integer deleteStatus);

    /**
     * Auto-generated documentation
     */
    List<PmsProduct> list(String keyword);
}
