package com.nexusengine.core.admin.catalog.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.catalog.repository.ProductPriceChangeRepository;


import com.nexusengine.core.domain.catalog.entity.ProductPriceChange;

import org.springframework.util.StringUtils;
import com.nexusengine.core.domain.catalog.repository.ProductSkuRepository;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
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
    private final com.nexusengine.core.domain.catalog.repository.ProductRepository productRepository;
    private final com.nexusengine.core.admin.security.AdminTenantContext adminTenantContext;

    @Override
    public List<ProductSku> getList(Long pid, String keyword) {
        Product product = productRepository.findById(pid).orElse(null);
        if (product != null) {
            adminTenantContext.assertBrandAccess(product.getBrandId());
        }
        if (StringUtils.hasText(keyword)) {
            return skuStockRepository.findByProductIdAndSkuCodeContaining(pid, keyword);
        }
        return skuStockRepository.findByProductId(pid);
    }

    @Override
    public int update(Long pid, List<ProductSku> skuStockList) {
        Product product = productRepository.findById(pid).orElse(null);
        if (product != null) {
            adminTenantContext.assertBrandAccess(product.getBrandId());
        }
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
            if (oldSku != null && newSku.getListPrice() != null && oldSku.getListPrice() != null && newSku.getListPrice().compareTo(oldSku.getListPrice()) != 0) {
                ProductPriceChange log = new ProductPriceChange();
                log.setProductId(pid);
                log.setCreateTime(java.time.OffsetDateTime.now());
                log.setOperateMan(username);
                log.setPriceOld(oldSku.getListPrice());
                log.setPriceNew(newSku.getListPrice());
                productAuditLogRepository.save(log);
            }
        }

        skuStockRepository.saveAll(filterSkuList);
        return filterSkuList.size();
    }
}
