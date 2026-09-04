package com.nexusengine.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.repository.SmsHomeBrandRepository;
import com.nexusengine.core.model.SmsHomeBrand;
import com.nexusengine.core.service.SmsHomeBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsHomeBrandServiceImpl implements SmsHomeBrandService {
    @Autowired
    private SmsHomeBrandRepository homeBrandRepository;
    
    @Override
    public int create(List<SmsHomeBrand> homeBrandList) {
        for (SmsHomeBrand smsHomeBrand : homeBrandList) {
            smsHomeBrand.setRecommendStatus(1);
            smsHomeBrand.setSort(0);
            homeBrandRepository.save(smsHomeBrand);
        }
        return homeBrandList.size();
    }

    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeBrand homeBrand = homeBrandRepository.findById(id).orElse(new SmsHomeBrand());
        homeBrand.setId(id);
        homeBrand.setSort(sort);
        homeBrandRepository.save(homeBrand);
        return 1;
    }

    @Override
    public int delete(List<Long> ids) {
        homeBrandRepository.deleteAllById(ids);
        return ids.size();
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        List<SmsHomeBrand> list = homeBrandRepository.findAllById(ids);
        for(SmsHomeBrand record : list){
            record.setRecommendStatus(recommendStatus);
            homeBrandRepository.save(record);
        }
        return list.size();
    }

    @Override
    public List<SmsHomeBrand> list(String brandName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        return homeBrandRepository.findAll(PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "sort"))).getContent();
    }
}
