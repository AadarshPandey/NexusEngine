package com.nexusengine.core.admin.catalog.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.catalog.repository.ProductAttributeValueRepository;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import com.nexusengine.core.domain.catalog.repository.ProductApprovalEventRepository;
import com.nexusengine.core.domain.catalog.repository.ProductSkuRepository;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import com.nexusengine.core.domain.catalog.repository.ProductPriceChangeRepository;
import com.nexusengine.core.domain.catalog.entity.ProductPriceChange;
import com.nexusengine.core.domain.iam.entity.AdminUser;
import com.nexusengine.core.domain.catalog.entity.ProductAttributeValue;
import com.nexusengine.core.domain.promotion.entity.SpendThresholdDiscount;
import com.nexusengine.core.domain.promotion.repository.QuantityTierDiscountRepository;
import com.nexusengine.core.domain.promotion.repository.SpendThresholdDiscountRepository;
import com.nexusengine.core.domain.catalog.entity.ProductApprovalEvent;
import com.nexusengine.core.domain.promotion.entity.QuantityTierDiscount;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.nexusengine.core.admin.iam.service.AdminUserService;
import com.nexusengine.core.admin.catalog.dto.ProductParam;
import com.nexusengine.core.admin.catalog.dto.ProductQueryParam;
import com.nexusengine.core.admin.catalog.dto.ProductResult;


/**
 * Product management Service implementation
 */
@Service("adminProductServiceImpl")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final QuantityTierDiscountRepository productVolumeDiscountRepository;
    private final SpendThresholdDiscountRepository productSpendDiscountRepository;
    private final ProductSkuRepository skuStockRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;


    private final ProductApprovalEventRepository productApprovalHistoryRepository;
    private final ProductPriceChangeRepository productAuditLogRepository;

    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public int create(ProductParam productParam) {
        Product product = new Product();
        BeanUtils.copyProperties(productParam, product);
        product.setId(null);
        productRepository.save(product);
        Long productId = product.getId();
        saveProductLadderList(productParam.productVolumeDiscountList(), productId);
        saveProductFullReductionList(productParam.productSpendDiscountList(), productId);
        handleSkuStockCode(productParam.skuStockList(), productId);
        if (productParam.skuStockList() == null || productParam.skuStockList().isEmpty()) {
            ProductSku defaultSku = new ProductSku();
            defaultSku.setProductId(productId);
            defaultSku.setSkuCode(java.util.UUID.randomUUID().toString().replace("-","").substring(0,20));
            defaultSku.setListPrice(java.math.BigDecimal.ZERO);
            defaultSku.setOnHandQty(0);
            skuStockRepository.save(defaultSku);
        } else {
            saveSkuStockList(productParam.skuStockList(), productId);
        }
        saveProductAttributeValueList(productParam.productAttributeValueList(), productId);


        return 1;
    }

    private void handleSkuStockCode(List<ProductSku> skuStockList, Long productId) {
        if (CollectionUtils.isEmpty(skuStockList)) return;
        for (int i = 0; i < skuStockList.size(); i++) {
            ProductSku skuStock = skuStockList.get(i);
            if (!StringUtils.hasText(skuStock.getSkuCode())) {
                DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyyMMdd");
                StringBuilder sb = new StringBuilder();
                sb.append(java.time.LocalDateTime.now().format(sdf));
                sb.append(String.format("%04d", productId));
                sb.append(String.format("%03d", i + 1));
                skuStock.setSkuCode(sb.toString());
            }
        }
    }

    @Override
    public ProductResult getUpdateInfo(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return null;
        return new ProductResult(new ProductParam(product, productVolumeDiscountRepository.findByProductId(id), productSpendDiscountRepository.findByProductId(id), skuStockRepository.findByProductId(id), productAttributeValueRepository.findByProductId(id)), null);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public int update(Long id, ProductParam productParam) {
        Product product = new Product();
        BeanUtils.copyProperties(productParam, product);
        product.setId(id);
        productRepository.save(product);
        productVolumeDiscountRepository.deleteByProductId(id);
        saveProductLadderList(productParam.productVolumeDiscountList(), id);
        productSpendDiscountRepository.deleteByProductId(id);
        saveProductFullReductionList(productParam.productSpendDiscountList(), id);
        handleUpdateSkuStockList(id, productParam);
        productAttributeValueRepository.deleteByProductId(id);
        saveProductAttributeValueList(productParam.productAttributeValueList(), id);


        return 1;
    }

    private void handleUpdateSkuStockList(Long id, ProductParam productParam) {
        List<ProductSku> currSkuList = productParam.skuStockList();
        if (currSkuList == null || currSkuList.isEmpty()) {
            List<ProductSku> existing = skuStockRepository.findByProductId(id);
            if (existing.isEmpty()) {
                ProductSku defaultSku = new ProductSku();
                defaultSku.setProductId(id);
                defaultSku.setSkuCode(java.util.UUID.randomUUID().toString().replace("-","").substring(0,20));
                defaultSku.setListPrice(java.math.BigDecimal.ZERO);
                defaultSku.setOnHandQty(0);
                skuStockRepository.save(defaultSku);
            }
            return;
        }
        List<ProductSku> oriStuList = skuStockRepository.findByProductId(id);
        List<ProductSku> insertSkuList = currSkuList.stream().filter(item -> item.getId() == null).collect(Collectors.toList());
        List<ProductSku> updateSkuList = currSkuList.stream().filter(item -> item.getId() != null).collect(Collectors.toList());
        List<Long> updateSkuIds = updateSkuList.stream().map(ProductSku::getId).collect(Collectors.toList());
        List<ProductSku> removeSkuList = oriStuList.stream().filter(item -> !updateSkuIds.contains(item.getId())).collect(Collectors.toList());
        handleSkuStockCode(insertSkuList, id);
        handleSkuStockCode(updateSkuList, id);
        if (!insertSkuList.isEmpty()) {
            for (ProductSku sku : insertSkuList) {
                sku.setProductId(id);
            }
            skuStockRepository.saveAll(insertSkuList);
        }
        if (!removeSkuList.isEmpty()) {
            skuStockRepository.deleteAll(removeSkuList);
        }
        if (!updateSkuList.isEmpty()) {
            skuStockRepository.saveAll(updateSkuList);
        }
    }

    private final AdminUserService adminService;

    @Override
    public org.springframework.data.domain.Page<Product> list(ProductQueryParam queryParam, Integer pageSize, Integer pageNum) {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        AdminUser admin = adminService.getAdminByUsername(username);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(pageNum - 1 > 0 ? pageNum - 1 : 0, pageSize, Sort.by(Sort.Direction.ASC, "id"));

        return productRepository.findAll((root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            if (queryParam.publishStatus() != null) {
                predicates.add(cb.equal(root.get("isPublished"), queryParam.publishStatus() == 1));
            }
            if (queryParam.verifyStatus() != null) {
                predicates.add(cb.equal(root.get("approvalStatus"), queryParam.verifyStatus()));
            }
            if (StringUtils.hasText(queryParam.keyword())) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + queryParam.keyword().toLowerCase() + "%"));
            }
            if (StringUtils.hasText(queryParam.productSn())) {
                predicates.add(cb.equal(root.get("productCode"), queryParam.productSn()));
            }
            if (queryParam.brandId() != null) {
                predicates.add(cb.equal(root.get("brandId"), queryParam.brandId()));
            }
            if (queryParam.productCategoryId() != null) {
                predicates.add(cb.equal(root.get("productCategoryId"), queryParam.productCategoryId()));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, pageable);
    }

    private void checkVendorAuthorization(List<Long> ids) {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        AdminUser admin = adminService.getAdminByUsername(username);
    }

    @Override
    public int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail) {
        checkVendorAuthorization(ids);
        List<Product> products = productRepository.findAllById(ids);
        for (Product product : products) {
            product.setApprovalStatus(verifyStatus);
            productRepository.save(product);
        }
        List<ProductApprovalEvent> records = new ArrayList<>();
        for (Long id : ids) {
            ProductApprovalEvent record = new ProductApprovalEvent();
            record.setProductId(id);
            record.setCreateTime(java.time.OffsetDateTime.now());
            record.setDetail(detail);
            record.setStatus(verifyStatus);
            String username = "admin";
            try {
                username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
            } catch(Exception e) {
                LOGGER.warn("Failed to get username from security context", e);
            }
            record.setReviewerName(username);
            records.add(record);
        }
        productApprovalHistoryRepository.saveAll(records);
        return products.size();
    }

    @Override
    public int updatePublishStatus(List<Long> ids, Integer publishStatus) {
        checkVendorAuthorization(ids);
        List<Product> products = productRepository.findAllById(ids);
        for (Product p : products) {
            p.setIsPublished(publishStatus != null && publishStatus == 1);
            productRepository.save(p);
        }
        return products.size();
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        checkVendorAuthorization(ids);
        List<Product> products = productRepository.findAllById(ids);
        for (Product p : products) {
            p.setIsFeatured(recommendStatus != null && recommendStatus == 1);
            productRepository.save(p);
        }
        return products.size();
    }

    @Override
    public int updateNewStatus(List<Long> ids, Integer newStatus) {
        checkVendorAuthorization(ids);
        List<Product> products = productRepository.findAllById(ids);
        for (Product p : products) {
            p.setIsNewArrival(newStatus != null && newStatus == 1);
            productRepository.save(p);
        }
        return products.size();
    }

    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        checkVendorAuthorization(ids);
        List<Product> products = productRepository.findAllById(ids);
        for (Product p : products) {
            p.setDeletedAt(deleteStatus != null && deleteStatus == 1 ? java.time.Instant.now() : null);
            productRepository.save(p);
        }
        return products.size();
    }

    @Override
    public List<Product> list(String keyword) {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        AdminUser admin = adminService.getAdminByUsername(username);

        boolean hasKeyword = StringUtils.hasText(keyword);

        if (hasKeyword) {
            return productRepository.findByNameContaining(keyword);
        }
        return productRepository.findAll(PageRequest.of(0, 1000)).getContent();
    }


    private void saveProductLadderList(List<QuantityTierDiscount> dataList, Long productId) {
        if (CollectionUtils.isEmpty(dataList)) return;
        for (QuantityTierDiscount item : dataList) {
            item.setId(null);
            item.setProductId(productId);
        }
        productVolumeDiscountRepository.saveAll(dataList);
    }

    private void saveProductFullReductionList(List<SpendThresholdDiscount> dataList, Long productId) {
        if (CollectionUtils.isEmpty(dataList)) return;
        for (SpendThresholdDiscount item : dataList) {
            item.setId(null);
            item.setProductId(productId);
        }
        productSpendDiscountRepository.saveAll(dataList);
    }

    private void saveSkuStockList(List<ProductSku> dataList, Long productId) {
        if (CollectionUtils.isEmpty(dataList)) return;
        for (ProductSku item : dataList) {
            item.setId(null);
            item.setProductId(productId);
        }
        skuStockRepository.saveAll(dataList);
    }

    private void saveProductAttributeValueList(List<ProductAttributeValue> dataList, Long productId) {
        if (CollectionUtils.isEmpty(dataList)) return;
        for (ProductAttributeValue item : dataList) {
            item.setId(null);
            item.setProductId(productId);
        }
        productAttributeValueRepository.saveAll(dataList);
    }


    @Override
    public java.util.List<ProductPriceChange> getAuditLog(Long id) {
        return productAuditLogRepository.findByProductId(id);
    }

    @Override
    public java.util.List<ProductApprovalEvent> getApprovalHistory(Long id) {
        return productApprovalHistoryRepository.findByProductId(id);
    }

}
