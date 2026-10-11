package com.nexusengine.core.admin.ordering.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.ordering.repository.ReturnRequestRepository;

import com.nexusengine.core.admin.ordering.dto.OrderStatusUpdateParam;
import com.nexusengine.core.admin.ordering.dto.ReturnRequestQueryParam;
import com.nexusengine.core.admin.ordering.dto.ReturnRequestResult;
import com.nexusengine.core.domain.ordering.entity.ReturnRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDateTime;
import java.util.List;


@Service("adminReturnRequestServiceImpl")
@RequiredArgsConstructor
public class ReturnRequestServiceImpl implements ReturnRequestService {
    private final ReturnRequestRepository returnApplyRepository;

    @Override
    public List<ReturnRequest> list(ReturnRequestQueryParam queryParam, Integer pageSize, Integer pageNum) {
        return returnApplyRepository.findAll(PageRequest.of(0, 1000)).getContent();
    }

    @Override
    public int delete(List<Long> ids) {
        returnApplyRepository.deleteAllById(ids);
        return ids.size();
    }

    @Override
    public int updateStatus(Long id, OrderStatusUpdateParam statusParam) {
        ReturnRequest returnApply = returnApplyRepository.findById(id).orElse(null);
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
    public ReturnRequestResult getItem(Long id) {
        ReturnRequest apply = returnApplyRepository.findById(id).orElse(null);
        if (apply == null) return null;
        return new ReturnRequestResult(apply, null);
    }
}
