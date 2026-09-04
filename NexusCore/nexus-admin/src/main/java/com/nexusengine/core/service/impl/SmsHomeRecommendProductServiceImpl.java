package com.nexusengine.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.repository.SmsHomeRecommendProductRepository;
import com.nexusengine.core.model.SmsHomeRecommendProduct;
import com.nexusengine.core.service.SmsHomeRecommendProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsHomeRecommendProductServiceImpl implements SmsHomeRecommendProductService {
    @Autowired
    private SmsHomeRecommendProductRepository homeRecommendProductRepository;
    
    @Override
    public int create(List<SmsHomeRecommendProduct> homeRecommendProductList) {
        for (SmsHomeRecommendProduct recommendProduct : homeRecommendProductList) {
            recommendProduct.setRecommendStatus(1);
            recommendProduct.setSort(0);
            homeRecommendProductRepository.save(recommendProduct);
        }
        return homeRecommendProductList.size();
    }

    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeRecommendProduct recommendProduct = homeRecommendProductRepository.findById(id).orElse(new SmsHomeRecommendProduct());
        recommendProduct.setId(id);
        recommendProduct.setSort(sort);
        homeRecommendProductRepository.save(recommendProduct);
        return 1;
    }

    @Override
    public int delete(List<Long> ids) {
        homeRecommendProductRepository.deleteAllById(ids);
        return ids.size();
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        List<SmsHomeRecommendProduct> list = homeRecommendProductRepository.findAllById(ids);
        for(SmsHomeRecommendProduct record : list){
            record.setRecommendStatus(recommendStatus);
            homeRecommendProductRepository.save(record);
        }
        return list.size();
    }

    @Override
    public List<SmsHomeRecommendProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        return homeRecommendProductRepository.findAll(PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "sort"))).getContent();
    }
}
