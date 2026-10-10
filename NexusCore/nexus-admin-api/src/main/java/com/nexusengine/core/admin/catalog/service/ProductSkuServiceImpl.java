package com.nexusengine.core.admin.catalog.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.catalog.repository.ProductPriceChangeRepository;

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
import com.nexusengine.core.domain.catalog.entity.ProductPriceChange;

import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.domain.catalog.repository.ProductSkuRepository;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * SKU stock management Service implementation
 */
@Service
@RequiredArgsConstructor
public class ProductSkuServiceImpl implements ProductSkuService {
    private final ProductSkuRepository skuStockRepository;
    private final ProductPriceChangeRepository productAuditLogRepository;

    @Override
    public List<ProductSku> getList(Long pid, String keyword) {
        if (!StrUtil.isEmpty(keyword)) {
            return skuStockRepository.findByProductIdAndSkuCodeContaining(pid, keyword);
        }
        return skuStockRepository.findByProductId(pid);
    }

    @Override
    public int update(Long pid, List<ProductSku> skuStockList) {
        List<ProductSku> filterSkuList = skuStockList.stream()
                .filter(item -> pid.equals(item.getProductId()))
                .collect(Collectors.toList());
        
        // Fetch old to log changes
        List<ProductSku> oldSkus = skuStockRepository.findByProductId(pid);
        String username = "admin";
        try {
            username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        } catch(Exception e) {
            org.slf4j.LoggerFactory.getLogger(ProductSkuServiceImpl.class).warn("Failed to get username from security context", e);
        }

        for (ProductSku newSku : filterSkuList) {
            ProductSku oldSku = oldSkus.stream().filter(o -> o.getId().equals(newSku.getId())).findFirst().orElse(null);
            if (oldSku != null && newSku.getPrice() != null && oldSku.getPrice() != null && newSku.getPrice().compareTo(oldSku.getPrice()) != 0) {
                ProductPriceChange log = new ProductPriceChange();
                log.setProductId(pid);
                log.setCreateTime(java.time.OffsetDateTime.now());
                log.setOperateMan(username);
                log.setPriceOld(oldSku.getPrice());
                log.setPriceNew(newSku.getPrice());
                productAuditLogRepository.save(log);
            }
        }
        
        skuStockRepository.saveAll(filterSkuList);
        return filterSkuList.size();
    }
}
