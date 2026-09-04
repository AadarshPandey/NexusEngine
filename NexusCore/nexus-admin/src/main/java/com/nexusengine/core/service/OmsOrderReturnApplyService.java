package com.nexusengine.core.service;

import com.nexusengine.core.dto.OmsOrderReturnApplyResult;
import com.nexusengine.core.dto.OmsReturnApplyQueryParam;
import com.nexusengine.core.dto.OmsUpdateStatusParam;
import com.nexusengine.core.model.OmsOrderReturnApply;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/10/18.
 */
public interface OmsOrderReturnApplyService {
    /**
     * Auto-generated documentation
     */
    List<OmsOrderReturnApply> list(OmsReturnApplyQueryParam queryParam, Integer pageSize, Integer pageNum);

    /**
     * Auto-generated documentation
     */
    int delete(List<Long> ids);

    /**
     * Auto-generated documentation
     */
    int updateStatus(Long id, OmsUpdateStatusParam statusParam);

    /**
     * Auto-generated documentation
     */
    OmsOrderReturnApplyResult getItem(Long id);
}
