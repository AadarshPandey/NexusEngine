package com.nexusengine.core.admin.ordering.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.ordering.repository.ReturnReasonRepository;

import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;
import com.nexusengine.core.admin.catalog.dto.*;
import com.nexusengine.core.admin.catalog.service.*;
import com.nexusengine.core.admin.analytics.service.*;
import com.nexusengine.core.admin.ordering.dto.*;
import com.nexusengine.core.admin.ordering.service.*;
import com.nexusengine.core.admin.promotion.dto.*;
import com.nexusengine.core.admin.promotion.service.*;
import com.nexusengine.core.admin.ai.service.*;
import com.nexusengine.core.admin.infra.dto.*;
import com.nexusengine.core.admin.infra.service.*;
import com.nexusengine.core.admin.iam.dto.*;
import com.nexusengine.core.admin.iam.service.*;
import com.nexusengine.core.domain.ordering.entity.ReturnReason;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReturnReasonServiceImpl implements ReturnReasonService {
    private final ReturnReasonRepository returnReasonRepository;

    @Override
    public int create(ReturnReason returnReason) {
        returnReason.setCreatedAt(java.time.OffsetDateTime.now());
        returnReasonRepository.save(returnReason);
        return 1;
    }

    @Override
    public int update(Long id, ReturnReason returnReason) {
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
    public List<ReturnReason> list(Integer pageSize, Integer pageNum) {
        return returnReasonRepository.findAll(PageRequest.of(pageNum > 0 ? pageNum - 1 : 0, pageSize, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "displayOrder"))).getContent();
    }

    @Override
    public int updateStatus(List<Long> ids, Integer status) {
        if (!status.equals(0) && !status.equals(1)) return 0;
        List<ReturnReason> reasons = returnReasonRepository.findAllById(ids);
        for (ReturnReason reason : reasons) {
            reason.setStatus(status);
            returnReasonRepository.save(reason);
        }
        return reasons.size();
    }

    @Override
    public ReturnReason getItem(Long id) {
        return returnReasonRepository.findById(id).orElse(null);
    }
}
