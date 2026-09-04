package com.nexusengine.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.repository.PmsSkuStockRepository;
import com.nexusengine.core.model.PmsSkuStock;
import com.nexusengine.core.service.PmsSkuStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SKU stock management Service implementation
 */
@Service
public class PmsSkuStockServiceImpl implements PmsSkuStockService {
    @Autowired
    private PmsSkuStockRepository skuStockRepository;

    @Override
    public List<PmsSkuStock> getList(Long pid, String keyword) {
        if (!StrUtil.isEmpty(keyword)) {
            return skuStockRepository.findByProductIdAndSkuCodeContaining(pid, keyword);
        }
        return skuStockRepository.findByProductId(pid);
    }

    @Override
    public int update(Long pid, List<PmsSkuStock> skuStockList) {
        List<PmsSkuStock> filterSkuList = skuStockList.stream()
                .filter(item -> pid.equals(item.getProductId()))
                .collect(Collectors.toList());
        skuStockRepository.saveAll(filterSkuList);
        return filterSkuList.size();
    }
}
