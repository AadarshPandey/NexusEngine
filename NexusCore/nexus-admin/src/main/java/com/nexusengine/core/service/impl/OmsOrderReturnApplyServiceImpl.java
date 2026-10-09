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
import org.springframework.data.domain.PageRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
@lombok.RequiredArgsConstructor
public class OmsOrderReturnApplyServiceImpl implements OmsOrderReturnApplyService {
    private final OmsOrderReturnApplyRepository returnApplyRepository;

    @Override
    public List<OmsOrderReturnApply> list(OmsReturnApplyQueryParam queryParam, Integer pageSize, Integer pageNum) {
        return returnApplyRepository.findAll(PageRequest.of(0, 1000)).getContent();
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
        Integer status = statusParam.status();
        if (status.equals(1)) {
            returnApply.setStatus(1);
            returnApply.setReturnAmount(statusParam.returnAmount());
            returnApply.setCompanyAddressId(statusParam.companyAddressId());
            returnApply.setHandleTime(java.time.OffsetDateTime.now());
            returnApply.setHandleMan(statusParam.handleMan());
            returnApply.setHandleNote(statusParam.handleNote());
        } else if (status.equals(2)) {
            returnApply.setStatus(2);
            returnApply.setReceiveTime(java.time.OffsetDateTime.now());
            returnApply.setReceiveMan(statusParam.receiveMan());
            returnApply.setReceiveNote(statusParam.receiveNote());
        } else if (status.equals(3)) {
            returnApply.setStatus(3);
            returnApply.setHandleTime(java.time.OffsetDateTime.now());
            returnApply.setHandleMan(statusParam.handleMan());
            returnApply.setHandleNote(statusParam.handleNote());
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
        return new OmsOrderReturnApplyResult(apply, null);
    }
}
