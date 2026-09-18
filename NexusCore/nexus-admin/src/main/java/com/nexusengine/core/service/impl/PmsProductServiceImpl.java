package com.nexusengine.core.service.impl;
import org.springframework.data.domain.Sort;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.dto.PmsProductParam;
import com.nexusengine.core.dto.PmsProductQueryParam;
import com.nexusengine.core.dto.PmsProductResult;
import com.nexusengine.core.repository.*;
import com.nexusengine.core.model.*;
import com.nexusengine.core.service.PmsProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Product management Service implementation
 */
@Service
@lombok.RequiredArgsConstructor
public class PmsProductServiceImpl implements PmsProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PmsProductServiceImpl.class);
    private final PmsProductRepository productRepository;
    private final PmsProductLadderRepository productLadderRepository;
    private final PmsProductFullReductionRepository productFullReductionRepository;
    private final PmsSkuStockRepository skuStockRepository;
    private final PmsProductAttributeValueRepository productAttributeValueRepository;
    
    
    private final PmsProductVerifyRecordRepository productVerifyRecordRepository;
    private final com.nexusengine.core.repository.PmsProductOperateLogRepository productOperateLogRepository;

    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public int create(PmsProductParam productParam) {
        PmsProduct product = new PmsProduct();
        BeanUtils.copyProperties(productParam, product);
        product.setId(null);
        productRepository.save(product);
        Long productId = product.getId();
        saveProductLadderList(productParam.getProductLadderList(), productId);
        saveProductFullReductionList(productParam.getProductFullReductionList(), productId);
        handleSkuStockCode(productParam.getSkuStockList(), productId);
        if (CollUtil.isEmpty(productParam.getSkuStockList())) {
            PmsSkuStock defaultSku = new PmsSkuStock();
            defaultSku.setProductId(productId);
            defaultSku.setSkuCode(java.util.UUID.randomUUID().toString().replace("-","").substring(0,20));
            defaultSku.setPrice(productParam.getPrice());
            defaultSku.setStock(productParam.getStock() != null ? productParam.getStock() : 0);
            skuStockRepository.save(defaultSku);
        } else {
            saveSkuStockList(productParam.getSkuStockList(), productId);
        }
        saveProductAttributeValueList(productParam.getProductAttributeValueList(), productId);
        
        
        return 1;
    }

    private void handleSkuStockCode(List<PmsSkuStock> skuStockList, Long productId) {
        if (CollectionUtils.isEmpty(skuStockList)) return;
        for (int i = 0; i < skuStockList.size(); i++) {
            PmsSkuStock skuStock = skuStockList.get(i);
            if (StrUtil.isEmpty(skuStock.getSkuCode())) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                StringBuilder sb = new StringBuilder();
                sb.append(sdf.format(new Date()));
                sb.append(String.format("%04d", productId));
                sb.append(String.format("%03d", i + 1));
                skuStock.setSkuCode(sb.toString());
            }
        }
    }

    @Override
    public PmsProductResult getUpdateInfo(Long id) {
        PmsProduct product = productRepository.findById(id).orElse(null);
        if (product == null) return null;
        PmsProductResult result = new PmsProductResult();
        BeanUtils.copyProperties(product, result);
        return result;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public int update(Long id, PmsProductParam productParam) {
        PmsProduct product = new PmsProduct();
        BeanUtils.copyProperties(productParam, product);
        product.setId(id);
        productRepository.save(product);
        productLadderRepository.deleteByProductId(id);
        saveProductLadderList(productParam.getProductLadderList(), id);
        productFullReductionRepository.deleteByProductId(id);
        saveProductFullReductionList(productParam.getProductFullReductionList(), id);
        handleUpdateSkuStockList(id, productParam);
        productAttributeValueRepository.deleteByProductId(id);
        saveProductAttributeValueList(productParam.getProductAttributeValueList(), id);
                
                
        return 1;
    }

    private void handleUpdateSkuStockList(Long id, PmsProductParam productParam) {
        List<PmsSkuStock> currSkuList = productParam.getSkuStockList();
        if (CollUtil.isEmpty(currSkuList)) {
            List<PmsSkuStock> existing = skuStockRepository.findByProductId(id);
            if (existing.isEmpty()) {
                PmsSkuStock defaultSku = new PmsSkuStock();
                defaultSku.setProductId(id);
                defaultSku.setSkuCode(java.util.UUID.randomUUID().toString().replace("-","").substring(0,20));
                defaultSku.setPrice(productParam.getPrice());
                defaultSku.setStock(productParam.getStock() != null ? productParam.getStock() : 0);
                skuStockRepository.save(defaultSku);
            } else {
                for (PmsSkuStock sku : existing) {
                    if (productParam.getPrice() != null) sku.setPrice(productParam.getPrice());
                    if (productParam.getStock() != null) sku.setStock(productParam.getStock());
                }
                skuStockRepository.saveAll(existing);
            }
            return;
        }
        List<PmsSkuStock> oriStuList = skuStockRepository.findByProductId(id);
        List<PmsSkuStock> insertSkuList = currSkuList.stream().filter(item -> item.getId() == null).collect(Collectors.toList());
        List<PmsSkuStock> updateSkuList = currSkuList.stream().filter(item -> item.getId() != null).collect(Collectors.toList());
        List<Long> updateSkuIds = updateSkuList.stream().map(PmsSkuStock::getId).collect(Collectors.toList());
        List<PmsSkuStock> removeSkuList = oriStuList.stream().filter(item -> !updateSkuIds.contains(item.getId())).collect(Collectors.toList());
        handleSkuStockCode(insertSkuList, id);
        handleSkuStockCode(updateSkuList, id);
        if (CollUtil.isNotEmpty(insertSkuList)) {
            for (PmsSkuStock sku : insertSkuList) {
                sku.setProductId(id);
            }
            skuStockRepository.saveAll(insertSkuList);
        }
        if (CollUtil.isNotEmpty(removeSkuList)) {
            skuStockRepository.deleteAll(removeSkuList);
        }
        if (CollUtil.isNotEmpty(updateSkuList)) {
            skuStockRepository.saveAll(updateSkuList);
        }
    }

    private final com.nexusengine.core.service.UmsAdminService adminService;

    @Override
    public org.springframework.data.domain.Page<PmsProduct> list(PmsProductQueryParam queryParam, Integer pageSize, Integer pageNum) {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        UmsAdmin admin = adminService.getAdminByUsername(username);
        Long vendorId = admin != null ? admin.getVendorId() : null;
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(pageNum - 1 > 0 ? pageNum - 1 : 0, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        
        return productRepository.findAll((root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            if (vendorId != null) {
                predicates.add(cb.equal(root.get("vendorId"), vendorId));
            }
            if (queryParam.getPublishStatus() != null) {
                predicates.add(cb.equal(root.get("publishStatus"), queryParam.getPublishStatus()));
            }
            if (queryParam.getVerifyStatus() != null) {
                predicates.add(cb.equal(root.get("verifyStatus"), queryParam.getVerifyStatus()));
            }
            if (StrUtil.isNotEmpty(queryParam.getKeyword())) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + queryParam.getKeyword().toLowerCase() + "%"));
            }
            if (StrUtil.isNotEmpty(queryParam.getProductSn())) {
                predicates.add(cb.equal(root.get("productSn"), queryParam.getProductSn()));
            }
            if (queryParam.getBrandId() != null) {
                predicates.add(cb.equal(root.get("brandId"), queryParam.getBrandId()));
            }
            if (queryParam.getProductCategoryId() != null) {
                predicates.add(cb.equal(root.get("productCategoryId"), queryParam.getProductCategoryId()));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, pageable);
    }

    private void checkVendorAuthorization(List<Long> ids) {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        UmsAdmin admin = adminService.getAdminByUsername(username);
        Long vendorId = admin != null ? admin.getVendorId() : null;
        
        if (vendorId != null) {
            List<PmsProduct> products = productRepository.findAllById(ids);
            for (PmsProduct product : products) {
                if (!vendorId.equals(product.getVendorId())) {
                    throw new com.nexusengine.core.common.exception.ApiException("Unauthorized: You can only modify your own products");
                }
            }
        }
    }

    @Override
        public int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail) {
        checkVendorAuthorization(ids);
        List<PmsProduct> products = productRepository.findAllById(ids);
        for (PmsProduct product : products) {
            product.setVerifyStatus(verifyStatus);
            productRepository.save(product);
        }
        List<PmsProductVerifyRecord> records = new ArrayList<>();
        for (Long id : ids) {
            PmsProductVerifyRecord record = new PmsProductVerifyRecord();
            record.setProductId(id);
            record.setCreateTime(new Date());
            record.setDetail(detail);
            record.setStatus(verifyStatus);
            String username = "admin";
            try {
                username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
            } catch(Exception e) {}
            record.setReviewerName(username);
            records.add(record);
        }
        productVerifyRecordRepository.saveAll(records);
        return products.size();
    }

    @Override
    public int updatePublishStatus(List<Long> ids, Integer publishStatus) {
        checkVendorAuthorization(ids);
        List<PmsProduct> products = productRepository.findAllById(ids);
        for (PmsProduct p : products) { p.setPublishStatus(publishStatus); productRepository.save(p); }
        return products.size();
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        checkVendorAuthorization(ids);
        List<PmsProduct> products = productRepository.findAllById(ids);
        for (PmsProduct p : products) { p.setRecommendStatus(recommendStatus); productRepository.save(p); }
        return products.size();
    }

    @Override
    public int updateNewStatus(List<Long> ids, Integer newStatus) {
        checkVendorAuthorization(ids);
        List<PmsProduct> products = productRepository.findAllById(ids);
        for (PmsProduct p : products) { p.setNewStatus(newStatus); productRepository.save(p); }
        return products.size();
    }

    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        checkVendorAuthorization(ids);
        List<PmsProduct> products = productRepository.findAllById(ids);
        for (PmsProduct p : products) { p.setDeleteStatus(deleteStatus); productRepository.save(p); }
        return products.size();
    }

    @Override
    public List<PmsProduct> list(String keyword) {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        UmsAdmin admin = adminService.getAdminByUsername(username);
        Long vendorId = admin != null ? admin.getVendorId() : null;
        
        boolean hasKeyword = StrUtil.isNotEmpty(keyword);
        
        if (vendorId != null) {
            if (hasKeyword) {
                return productRepository.findByVendorIdAndNameContaining(vendorId, keyword);
            }
            return productRepository.findByVendorId(vendorId);
        } else {
            if (hasKeyword) {
                return productRepository.findByNameContaining(keyword);
            }
            return productRepository.findAll(PageRequest.of(0, 1000)).getContent();
        }
    }


    private void saveProductLadderList(List<PmsProductLadder> dataList, Long productId) {
        if (CollectionUtils.isEmpty(dataList)) return;
        for (PmsProductLadder item : dataList) {
            item.setId(null);
            item.setProductId(productId);
        }
        productLadderRepository.saveAll(dataList);
    }

    private void saveProductFullReductionList(List<PmsProductFullReduction> dataList, Long productId) {
        if (CollectionUtils.isEmpty(dataList)) return;
        for (PmsProductFullReduction item : dataList) {
            item.setId(null);
            item.setProductId(productId);
        }
        productFullReductionRepository.saveAll(dataList);
    }

    private void saveSkuStockList(List<PmsSkuStock> dataList, Long productId) {
        if (CollectionUtils.isEmpty(dataList)) return;
        for (PmsSkuStock item : dataList) {
            item.setId(null);
            item.setProductId(productId);
        }
        skuStockRepository.saveAll(dataList);
    }

    private void saveProductAttributeValueList(List<PmsProductAttributeValue> dataList, Long productId) {
        if (CollectionUtils.isEmpty(dataList)) return;
        for (PmsProductAttributeValue item : dataList) {
            item.setId(null);
            item.setProductId(productId);
        }
        productAttributeValueRepository.saveAll(dataList);
    }


    @Override
    public java.util.List<com.nexusengine.core.model.PmsProductOperateLog> getOperateLog(Long id) {
        return productOperateLogRepository.findByProductId(id);
    }

    @Override
    public java.util.List<com.nexusengine.core.model.PmsProductVerifyRecord> getVerifyRecord(Long id) {
        return productVerifyRecordRepository.findByProductId(id);
    }

}
