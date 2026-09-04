package com.nexusengine.core.service.impl;

import com.nexusengine.core.repository.OmsOrderReturnReasonRepository;
import com.nexusengine.core.model.OmsOrderReturnReason;
import com.nexusengine.core.service.OmsOrderReturnReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class OmsOrderReturnReasonServiceImpl implements OmsOrderReturnReasonService {
    @Autowired
    private OmsOrderReturnReasonRepository returnReasonRepository;

    @Override
    public int create(OmsOrderReturnReason returnReason) {
        returnReason.setCreateTime(new Date());
        returnReasonRepository.save(returnReason);
        return 1;
    }

    @Override
    public int update(Long id, OmsOrderReturnReason returnReason) {
        returnReason.setId(id);
        returnReasonRepository.save(returnReason);
        return 1;
    }

    @Override
    public int delete(List<Long> ids) {
        returnReasonRepository.deleteAllById(ids);
        return ids.size();
    }

    @Override
    public List<OmsOrderReturnReason> list(Integer pageSize, Integer pageNum) {
        return returnReasonRepository.findAll(PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "sort"))).getContent();
    }

    @Override
    public int updateStatus(List<Long> ids, Integer status) {
        if (!status.equals(0) && !status.equals(1)) return 0;
        List<OmsOrderReturnReason> reasons = returnReasonRepository.findAllById(ids);
        for (OmsOrderReturnReason reason : reasons) {
            reason.setStatus(status);
            returnReasonRepository.save(reason);
        }
        return reasons.size();
    }

    @Override
    public OmsOrderReturnReason getItem(Long id) {
        return returnReasonRepository.findById(id).orElse(null);
    }
}
