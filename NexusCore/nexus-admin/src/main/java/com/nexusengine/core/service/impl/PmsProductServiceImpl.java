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
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public int create(PmsProductParam productParam) {
        PmsProduct product = new PmsProduct();
        BeanUtils.copyProperties(productParam, product);
        product.setId(null);
        productRepository.save(product);
        Long productId = product.getId();
        saveMemberPriceList(productParam.getMemberPriceList(), productId);
        saveProductLadderList(productParam.getProductLadderList(), productId);
        saveProductFullReductionList(productParam.getProductFullReductionList(), productId);
        handleSkuStockCode(productParam.getSkuStockList(), productId);
        saveSkuStockList(productParam.getSkuStockList(), productId);
        saveProductAttributeValueList(productParam.getProductAttributeValueList(), productId);
        saveSubjectProductRelationList(productParam.getSubjectProductRelationList(), productId);
        savePrefrenceAreaProductRelationList(productParam.getPrefrenceAreaProductRelationList(), productId);
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
        memberPriceRepository.deleteByProductId(id);
        saveMemberPriceList(productParam.getMemberPriceList(), id);
        productLadderRepository.deleteByProductId(id);
        saveProductLadderList(productParam.getProductLadderList(), id);
        productFullReductionRepository.deleteByProductId(id);
        saveProductFullReductionList(productParam.getProductFullReductionList(), id);
        handleUpdateSkuStockList(id, productParam);
        productAttributeValueRepository.deleteByProductId(id);
        saveProductAttributeValueList(productParam.getProductAttributeValueList(), id);
        subjectProductRelationRepository.deleteByProductId(id);
        saveSubjectProductRelationList(productParam.getSubjectProductRelationList(), id);
        prefrenceAreaProductRelationRepository.deleteByProductId(id);
        savePrefrenceAreaProductRelationList(productParam.getPrefrenceAreaProductRelationList(), id);
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
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(pageNum - 1 > 0 ? pageNum - 1 : 0, pageSize);
        if (vendorId != null) {
            return productRepository.findByVendorId(vendorId, pageable).getContent();
        }
        return productRepository.findAll(pageable).getContent();
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
            return productRepository.findAll(PageRequest.of(0, 1000)).getContent();
        }
    }

    private void saveMemberPriceList(List<PmsMemberPrice> dataList, Long productId) {
        if (CollectionUtils.isEmpty(dataList)) return;
        for (PmsMemberPrice item : dataList) {
            item.setId(null);
            item.setProductId(productId);
        }
        memberPriceRepository.saveAll(dataList);
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

    private void saveSubjectProductRelationList(List<CmsSubjectProductRelation> dataList, Long productId) {
        if (CollectionUtils.isEmpty(dataList)) return;
        for (CmsSubjectProductRelation item : dataList) {
            item.setId(null);
            item.setProductId(productId);
        }
        subjectProductRelationRepository.saveAll(dataList);
    }

    private void savePrefrenceAreaProductRelationList(List<CmsPrefrenceAreaProductRelation> dataList, Long productId) {
        if (CollectionUtils.isEmpty(dataList)) return;
        for (CmsPrefrenceAreaProductRelation item : dataList) {
            item.setId(null);
            item.setProductId(productId);
        }
        prefrenceAreaProductRelationRepository.saveAll(dataList);
    }
}
