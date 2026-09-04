package com.nexusengine.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.repository.SmsHomeNewProductRepository;
import com.nexusengine.core.model.SmsHomeNewProduct;
import com.nexusengine.core.service.SmsHomeNewProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsHomeNewProductServiceImpl implements SmsHomeNewProductService {
    @Autowired
    private SmsHomeNewProductRepository homeNewProductRepository;
    
    @Override
    public int create(List<SmsHomeNewProduct> homeNewProductList) {
        for (SmsHomeNewProduct smsHomeNewProduct : homeNewProductList) {
            smsHomeNewProduct.setRecommendStatus(1);
            smsHomeNewProduct.setSort(0);
            homeNewProductRepository.save(smsHomeNewProduct);
        }
        return homeNewProductList.size();
    }

    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeNewProduct homeNewProduct = homeNewProductRepository.findById(id).orElse(new SmsHomeNewProduct());
        homeNewProduct.setId(id);
        homeNewProduct.setSort(sort);
        homeNewProductRepository.save(homeNewProduct);
        return 1;
    }

    @Override
    public int delete(List<Long> ids) {
        homeNewProductRepository.deleteAllById(ids);
        return ids.size();
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        List<SmsHomeNewProduct> list = homeNewProductRepository.findAllById(ids);
        for (SmsHomeNewProduct record : list) {
            record.setRecommendStatus(recommendStatus);
            homeNewProductRepository.save(record);
        }
        return list.size();
    }

    @Override
    public List<SmsHomeNewProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        return homeNewProductRepository.findAll(PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "sort"))).getContent();
    }
}
