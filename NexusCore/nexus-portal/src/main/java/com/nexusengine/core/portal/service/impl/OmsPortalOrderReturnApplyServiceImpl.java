package com.nexusengine.core.portal.service.impl;

import com.nexusengine.core.model.OmsOrderReturnApply;
import com.nexusengine.core.portal.domain.OmsOrderReturnApplyParam;
import com.nexusengine.core.portal.service.OmsPortalOrderReturnApplyService;
import com.nexusengine.core.repository.OmsOrderReturnApplyRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Order return apply Service implementation
 */
@Service
public class OmsPortalOrderReturnApplyServiceImpl implements OmsPortalOrderReturnApplyService {
    @Autowired
    private OmsOrderReturnApplyRepository returnApplyRepository;

    @Override
    public int create(OmsOrderReturnApplyParam returnApply) {
        OmsOrderReturnApply realApply = new OmsOrderReturnApply();
        BeanUtils.copyProperties(returnApply, realApply);
        realApply.setCreateTime(new Date());
        realApply.setStatus(0);
        returnApplyRepository.save(realApply);
        return 1;
    }
}
