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
@lombok.RequiredArgsConstructor
public class PmsSkuStockServiceImpl implements PmsSkuStockService {
    private final PmsSkuStockRepository skuStockRepository;
    private final com.nexusengine.core.repository.PmsProductOperateLogRepository productOperateLogRepository;

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
        
        // Fetch old to log changes
        List<PmsSkuStock> oldSkus = skuStockRepository.findByProductId(pid);
        String username = "admin";
        try {
            username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        } catch(Exception e) {}

        for (PmsSkuStock newSku : filterSkuList) {
            PmsSkuStock oldSku = oldSkus.stream().filter(o -> o.getId().equals(newSku.getId())).findFirst().orElse(null);
            if (oldSku != null && newSku.getPrice() != null && oldSku.getPrice() != null && newSku.getPrice().compareTo(oldSku.getPrice()) != 0) {
                com.nexusengine.core.model.PmsProductOperateLog log = new com.nexusengine.core.model.PmsProductOperateLog();
                log.setProductId(pid);
                log.setCreateTime(new java.util.Date());
                log.setOperateMan(username);
                log.setPriceOld(oldSku.getPrice());
                log.setPriceNew(newSku.getPrice());
                productOperateLogRepository.save(log);
            }
        }
        
        skuStockRepository.saveAll(filterSkuList);
        return filterSkuList.size();
    }
}
