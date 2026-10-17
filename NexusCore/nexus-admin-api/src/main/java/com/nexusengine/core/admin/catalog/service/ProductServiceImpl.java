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
import com.nexusengine.core.domain.catalog.entity.ProductApprovalEvent;
import com.nexusengine.core.domain.catalog.entity.ProductMedia;
import com.nexusengine.core.domain.catalog.repository.ProductMediaRepository;
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
    private final ProductSkuRepository skuStockRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;
    private final ProductMediaRepository productMediaRepository;

    private final ProductApprovalEventRepository productApprovalHistoryRepository;
    private final ProductPriceChangeRepository productAuditLogRepository;
    private final com.nexusengine.core.admin.security.AdminTenantContext adminTenantContext;

    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public int create(ProductParam productParam) {
        Product product = new Product();
        BeanUtils.copyProperties(productParam, product);
        product.setId(null);
        adminTenantContext.currentBrandId().ifPresent(product::setBrandId);
        productRepository.save(product);
        Long productId = product.getId();
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
        List<ProductMedia> gallery = productParam.galleryList() != null ? productParam.galleryList() : product.getGalleryList();
        saveProductMediaList(gallery, productId);

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
        adminTenantContext.assertBrandAccess(product.getBrandId());
        List<ProductMedia> galleryList = productMediaRepository.findByProductIdOrderBySortOrderAsc(id);
        product.setGalleryList(galleryList);
        return new ProductResult(new ProductParam(product, skuStockRepository.findByProductId(id), productAttributeValueRepository.findByProductId(id), galleryList), null);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    public int update(Long id, ProductParam productParam) {
        Product rawProduct = productRepository.findById(id).orElse(null);
        if (rawProduct != null) {
            adminTenantContext.assertBrandAccess(rawProduct.getBrandId());
        }
        Product product = new Product();
        BeanUtils.copyProperties(productParam, product);
        product.setId(id);
        adminTenantContext.currentBrandId().ifPresent(product::setBrandId);
        productRepository.save(product);
        handleUpdateSkuStockList(id, productParam);
        productAttributeValueRepository.deleteByProductId(id);
        saveProductAttributeValueList(productParam.productAttributeValueList(), id);
        productMediaRepository.deleteByProductId(id);
        List<ProductMedia> gallery = productParam.galleryList() != null ? productParam.galleryList() : product.getGalleryList();
        saveProductMediaList(gallery, id);

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

        // Price change audit logging
        String username = "admin";
        try {
            var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null) username = auth.getName();
        } catch (Exception ignored) {}

        for (ProductSku newSku : updateSkuList) {
            ProductSku oldSku = oriStuList.stream().filter(o -> o.getId().equals(newSku.getId())).findFirst().orElse(null);
            if (oldSku != null && newSku.getListPrice() != null && oldSku.getListPrice() != null && newSku.getListPrice().compareTo(oldSku.getListPrice()) != 0) {
                ProductPriceChange log = new ProductPriceChange();
                log.setProductId(id);
                log.setCreateTime(java.time.OffsetDateTime.now());
                log.setOperateMan(username);
                log.setPriceOld(oldSku.getListPrice());
                log.setPriceNew(newSku.getListPrice());
                if (oldSku.getSalePrice() != null) log.setSalePriceOld(oldSku.getSalePrice());
                if (newSku.getSalePrice() != null) log.setSalePriceNew(newSku.getSalePrice());
                productAuditLogRepository.save(log);
            }
        }

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
            if (queryParam.isPublished() != null) {
                predicates.add(cb.equal(root.get("isPublished"), queryParam.isPublished() == 1));
            }
            if (queryParam.approvalStatus() != null) {
                predicates.add(cb.equal(root.get("approvalStatus"), queryParam.approvalStatus()));
            }
            if (StringUtils.hasText(queryParam.keyword())) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + queryParam.keyword().toLowerCase() + "%"));
            }
            if (StringUtils.hasText(queryParam.productCode())) {
                predicates.add(cb.equal(root.get("productCode"), queryParam.productCode()));
            }
            if (adminTenantContext.isBrandScoped()) {
                predicates.add(cb.equal(root.get("brandId"), adminTenantContext.currentBrandId().get()));
            } else if (queryParam.brandId() != null) {
                predicates.add(cb.equal(root.get("brandId"), queryParam.brandId()));
            }
            if (queryParam.productCategoryId() != null) {
                predicates.add(cb.equal(root.get("productCategoryId"), queryParam.productCategoryId()));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, pageable);
    }

    private void checkVendorAuthorization(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) return;
        adminTenantContext.currentBrandId().ifPresent(brandId -> {
            List<Product> products = productRepository.findAllById(ids);
            for (Product p : products) {
                if (p.getBrandId() == null || !brandId.equals(p.getBrandId())) {
                    throw new com.nexusengine.core.common.exception.ApiException(com.nexusengine.core.common.api.ResultCode.FORBIDDEN);
                }
            }
        });
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
        if (adminTenantContext.isBrandScoped()) {
            Long brandId = adminTenantContext.currentBrandId().get();
            return productRepository.findAll((root, query, cb) -> {
                var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
                predicates.add(cb.equal(root.get("brandId"), brandId));
                if (StringUtils.hasText(keyword)) {
                    predicates.add(cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
                }
                return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
            }, PageRequest.of(0, 1000)).getContent();
        }

        boolean hasKeyword = StringUtils.hasText(keyword);

        if (hasKeyword) {
            return productRepository.findByNameContaining(keyword);
        }
        return productRepository.findAll(PageRequest.of(0, 1000)).getContent();
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

    private void saveProductMediaList(List<ProductMedia> galleryList, Long productId) {
        if (CollectionUtils.isEmpty(galleryList)) return;
        int order = 1;
        for (ProductMedia media : galleryList) {
            media.setId(null);
            media.setProductId(productId);
            if (!StringUtils.hasText(media.getMediaType())) {
                media.setMediaType("image");
            }
            if (media.getSortOrder() == null) {
                media.setSortOrder(order++);
            }
        }
        productMediaRepository.saveAll(galleryList);
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
