package com.nexusengine.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.repository.PromotionalBannerRepository;
import com.nexusengine.core.model.PromotionalBanner;
import com.nexusengine.core.service.PromotionalBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@lombok.RequiredArgsConstructor
public class PromotionalBannerServiceImpl implements PromotionalBannerService {
    private final PromotionalBannerRepository advertiseRepository;

    @Override
    public int create(PromotionalBanner advertise) {
        advertise.setClickCount(0);
        advertise.setOrderCount(0);
        advertiseRepository.save(advertise);
        return 1;
    }

    @Override
    public int delete(List<Long> ids) {
        advertiseRepository.deleteAllById(ids);
        return ids.size();
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        PromotionalBanner record = advertiseRepository.findById(id).orElse(new PromotionalBanner());
        record.setId(id);
        record.setStatus(status);
        advertiseRepository.save(record);
        return 1;
    }

    @Override
    public PromotionalBanner getItem(Long id) {
        return advertiseRepository.findById(id).orElse(null);
    }

    @Override
    public int update(Long id, PromotionalBanner advertise) {
        advertise.setId(id);
        advertiseRepository.save(advertise);
        return 1;
    }

    @Override
    public List<PromotionalBanner> list(String name, Integer type, String endTime, Integer pageSize, Integer pageNum) {
        return advertiseRepository.findAll(PageRequest.of(pageNum > 0 ? pageNum - 1 : 0, pageSize, Sort.by(Sort.Direction.DESC, "sort"))).getContent();
    }
}
