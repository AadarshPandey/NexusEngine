package com.nexusengine.core.service.impl;

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
public class PmsProductServiceImpl implements PmsProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PmsProductServiceImpl.class);
    @Autowired
    private PmsProductRepository productRepository;
    @Autowired
    private PmsMemberPriceRepository memberPriceRepository;
    @Autowired
    private PmsProductLadderRepository productLadderRepository;
    @Autowired
    private PmsProductFullReductionRepository productFullReductionRepository;
    @Autowired
    private PmsSkuStockRepository skuStockRepository;
    @Autowired
    private PmsProductAttributeValueRepository productAttributeValueRepository;
    @Autowired
    private CmsSubjectProductRelationRepository subjectProductRelationRepository;
    @Autowired
    private CmsPrefrenceAreaProductRelationRepository prefrenceAreaProductRelationRepository;
    @Autowired
    private PmsProductVertifyRecordRepository productVertifyRecordRepository;

    @Override
    public int create(PmsProductParam productParam) {
        PmsProduct product = productParam;
        product.setId(null);
        productRepository.save(product);
        Long productId = product.getId();
        saveRelatedList(memberPriceRepository, productParam.getMemberPriceList(), productId);
        saveRelatedList(productLadderRepository, productParam.getProductLadderList(), productId);
        saveRelatedList(productFullReductionRepository, productParam.getProductFullReductionList(), productId);
        handleSkuStockCode(productParam.getSkuStockList(), productId);
        saveRelatedList(skuStockRepository, productParam.getSkuStockList(), productId);
        saveRelatedList(productAttributeValueRepository, productParam.getProductAttributeValueList(), productId);
        saveRelatedList(subjectProductRelationRepository, productParam.getSubjectProductRelationList(), productId);
        saveRelatedList(prefrenceAreaProductRelationRepository, productParam.getPrefrenceAreaProductRelationList(), productId);
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
    public int update(Long id, PmsProductParam productParam) {
        PmsProduct product = productParam;
        product.setId(id);
        productRepository.save(product);
        memberPriceRepository.deleteByProductId(id);
        saveRelatedList(memberPriceRepository, productParam.getMemberPriceList(), id);
        productLadderRepository.deleteByProductId(id);
        saveRelatedList(productLadderRepository, productParam.getProductLadderList(), id);
        productFullReductionRepository.deleteByProductId(id);
        saveRelatedList(productFullReductionRepository, productParam.getProductFullReductionList(), id);
        handleUpdateSkuStockList(id, productParam);
        productAttributeValueRepository.deleteByProductId(id);
        saveRelatedList(productAttributeValueRepository, productParam.getProductAttributeValueList(), id);
        subjectProductRelationRepository.deleteByProductId(id);
        saveRelatedList(subjectProductRelationRepository, productParam.getSubjectProductRelationList(), id);
        prefrenceAreaProductRelationRepository.deleteByProductId(id);
        saveRelatedList(prefrenceAreaProductRelationRepository, productParam.getPrefrenceAreaProductRelationList(), id);
        return 1;
    }

    private void handleUpdateSkuStockList(Long id, PmsProductParam productParam) {
        List<PmsSkuStock> currSkuList = productParam.getSkuStockList();
        if (CollUtil.isEmpty(currSkuList)) {
            skuStockRepository.deleteByProductId(id);
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

    @Autowired
    private com.nexusengine.core.service.UmsAdminService adminService;

    @Override
    public List<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum) {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        UmsAdmin admin = adminService.getAdminByUsername(username);
        Long vendorId = admin != null ? admin.getVendorId() : null;
        if (vendorId != null) {
            return productRepository.findByVendorId(vendorId);
        }
        return productRepository.findAll();
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
        List<PmsProductVertifyRecord> records = new ArrayList<>();
        for (Long id : ids) {
            PmsProductVertifyRecord record = new PmsProductVertifyRecord();
            record.setProductId(id);
            record.setCreateTime(new Date());
            record.setDetail(detail);
            record.setStatus(verifyStatus);
            record.setVertifyMan("admin");
            records.add(record);
        }
        productVertifyRecordRepository.saveAll(records);
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
        for (PmsProduct p : products) { p.setRecommandStatus(recommendStatus); productRepository.save(p); }
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
            return productRepository.findAll();
        }
    }

    /**
     * Set productId on each entity and save all via the repository
     */
    @SuppressWarnings("unchecked")
    private void saveRelatedList(Object repository, List dataList, Long productId) {
        try {
            if (CollectionUtils.isEmpty(dataList)) return;
            for (Object item : dataList) {
                Method setId = item.getClass().getMethod("setId", Long.class);
                setId.invoke(item, (Long) null);
                Method setProductId = item.getClass().getMethod("setProductId", Long.class);
                setProductId.invoke(item, productId);
            }
            Method saveAll = repository.getClass().getMethod("saveAll", Iterable.class);
            saveAll.invoke(repository, dataList);
        } catch (Exception e) {
            LOGGER.warn("Error saving product relations: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
