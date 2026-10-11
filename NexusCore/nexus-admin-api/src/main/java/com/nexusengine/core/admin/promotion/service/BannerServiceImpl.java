package com.nexusengine.core.admin.promotion.service;

import lombok.RequiredArgsConstructor;



import com.nexusengine.core.domain.promotion.repository.BannerRepository;
import com.nexusengine.core.domain.promotion.entity.Banner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {
    private final BannerRepository advertiseRepository;

    @Override
    public int create(Banner advertise) {
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
        Banner record = advertiseRepository.findById(id).orElse(new Banner());
        record.setId(id);
        record.setStatus(status);
        advertiseRepository.save(record);
        return 1;
    }

    @Override
    public Banner getItem(Long id) {
        return advertiseRepository.findById(id).orElse(null);
    }

    @Override
    public int update(Long id, Banner advertise) {
        advertise.setId(id);
        advertiseRepository.save(advertise);
        return 1;
    }

    @Override
    public List<Banner> list(String name, Integer type, String endTime, Integer pageSize, Integer pageNum) {
        return advertiseRepository.findAll(PageRequest.of(pageNum > 0 ? pageNum - 1 : 0, pageSize, Sort.by(Sort.Direction.DESC, "sort"))).getContent();
    }
}
