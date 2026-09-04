package com.nexusengine.core.service.impl;

import com.nexusengine.core.dto.OmsOrderReturnApplyResult;
import com.nexusengine.core.dto.OmsReturnApplyQueryParam;
import com.nexusengine.core.dto.OmsUpdateStatusParam;
import com.nexusengine.core.repository.OmsOrderReturnApplyRepository;
import com.nexusengine.core.model.OmsOrderReturnApply;
import com.nexusengine.core.service.OmsOrderReturnApplyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class OmsOrderReturnApplyServiceImpl implements OmsOrderReturnApplyService {
    @Autowired
    private OmsOrderReturnApplyRepository returnApplyRepository;

    @Override
    public List<OmsOrderReturnApply> list(OmsReturnApplyQueryParam queryParam, Integer pageSize, Integer pageNum) {
        return returnApplyRepository.findAll();
    }

    @Override
    public int delete(List<Long> ids) {
        returnApplyRepository.deleteAllById(ids);
        return ids.size();
    }

    @Override
    public int updateStatus(Long id, OmsUpdateStatusParam statusParam) {
        OmsOrderReturnApply returnApply = returnApplyRepository.findById(id).orElse(null);
        if (returnApply == null) return 0;
        Integer status = statusParam.getStatus();
        if (status.equals(1)) {
            returnApply.setStatus(1);
            returnApply.setReturnAmount(statusParam.getReturnAmount());
            returnApply.setCompanyAddressId(statusParam.getCompanyAddressId());
            returnApply.setHandleTime(new Date());
            returnApply.setHandleMan(statusParam.getHandleMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        } else if (status.equals(2)) {
            returnApply.setStatus(2);
            returnApply.setReceiveTime(new Date());
            returnApply.setReceiveMan(statusParam.getReceiveMan());
            returnApply.setReceiveNote(statusParam.getReceiveNote());
        } else if (status.equals(3)) {
            returnApply.setStatus(3);
            returnApply.setHandleTime(new Date());
            returnApply.setHandleMan(statusParam.getHandleMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        } else {
            return 0;
        }
        returnApplyRepository.save(returnApply);
        return 1;
    }

    @Override
    public OmsOrderReturnApplyResult getItem(Long id) {
        OmsOrderReturnApply apply = returnApplyRepository.findById(id).orElse(null);
        if (apply == null) return null;
        OmsOrderReturnApplyResult result = new OmsOrderReturnApplyResult();
        BeanUtils.copyProperties(apply, result);
        return result;
    }
}
