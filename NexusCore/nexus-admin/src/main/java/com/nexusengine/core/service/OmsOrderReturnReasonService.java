package com.nexusengine.core.service;

import com.nexusengine.core.model.OmsOrderReturnReason;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/10/17.
 */
public interface OmsOrderReturnReasonService {
    /**
     * Auto-generated documentation
     */
    int create(OmsOrderReturnReason returnReason);

    /**
     * Auto-generated documentation
     */
    int update(Long id, OmsOrderReturnReason returnReason);

    /**
     * Auto-generated documentation
     */
    int delete(List<Long> ids);

    /**
     * Auto-generated documentation
     */
    List<OmsOrderReturnReason> list(Integer pageSize, Integer pageNum);

    /**
     * Auto-generated documentation
     */
    int updateStatus(List<Long> ids, Integer status);

    /**
     * Auto-generated documentation
     */
    OmsOrderReturnReason getItem(Long id);
}
