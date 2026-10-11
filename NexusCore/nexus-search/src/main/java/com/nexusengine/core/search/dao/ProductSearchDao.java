package com.nexusengine.core.search.dao;


import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.entity.ProductAttributeValue;
import com.nexusengine.core.domain.catalog.repository.ProductAttributeValueRepository;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import com.nexusengine.core.search.domain.ProductDocument;
import com.nexusengine.core.search.domain.ProductAttributeValueDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Search product data access - converts JPA product data to ES product format
 */
@Repository
public class ProductSearchDao {
    private static final Logger log = LoggerFactory.getLogger(ProductSearchDao.class);

    private final ProductRepository productRepository;
    private final ProductAttributeValueRepository attributeValueRepository;
    private final com.nexusengine.core.domain.catalog.repository.CategoryRepository categoryRepository;
    private final com.nexusengine.core.domain.catalog.repository.ProductSkuRepository skuRepository;

    public ProductSearchDao(ProductRepository productRepository,
                        ProductAttributeValueRepository attributeValueRepository,
                        com.nexusengine.core.domain.catalog.repository.CategoryRepository categoryRepository,
                        com.nexusengine.core.domain.catalog.repository.ProductSkuRepository skuRepository) {
        this.productRepository = productRepository;
        this.attributeValueRepository = attributeValueRepository;
        this.categoryRepository = categoryRepository;
        this.skuRepository = skuRepository;
    }

    /**
     * Get all products formatted for Elasticsearch indexing
     * @param id if provided, returns only the product with this id; otherwise returns all
     */
    public List<ProductDocument> getAllEsProductList(Long id) {
        List<Product> products;
        if (id != null) {
            products = productRepository.findAllById(Collections.singletonList(id));
        } else {
            products = /* Intentional unbounded findAll for bulk ES indexing */ productRepository.findAll();
        }
        List<ProductDocument> result = new ArrayList<>();
        for (Product product : products) {
            ProductDocument esProduct = new ProductDocument();
            esProduct.setId(product.getId());
            esProduct.setProductSn(product.getProductSn());
            esProduct.setBrandId(product.getBrandId());
            esProduct.setBrandName(product.getBrandName());
            esProduct.setProductCategoryId(product.getProductCategoryId());
            esProduct.setPic(product.getPrimaryImageUrl());
            esProduct.setName(product.getName());

            String keywords = product.getKeywords() != null ? product.getKeywords() : "";
            try {
                if (product.getProductCategoryId() != null) {
                    com.nexusengine.core.domain.catalog.entity.Category cat = categoryRepository.findById(product.getProductCategoryId()).orElse(null);
                    if (cat != null && cat.getName() != null) {
                        keywords += " " + cat.getName();
                        if (cat.getParentId() != null && cat.getParentId() > 0) {
                            com.nexusengine.core.domain.catalog.entity.Category parentCat = categoryRepository.findById(cat.getParentId()).orElse(null);
                            if (parentCat != null && parentCat.getName() != null) {
                                keywords += " " + parentCat.getName();
                            }
                        }
                    }
                }
            } catch (Exception e) {}
            esProduct.setKeywords(keywords.trim());

            List<com.nexusengine.core.domain.catalog.entity.ProductSku> skus = skuRepository.findByProductId(product.getId());
            BigDecimal price = skus.stream()
                .map(com.nexusengine.core.domain.catalog.entity.ProductSku::getListPrice)
                .filter(java.util.Objects::nonNull)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
            Integer stock = skus.stream()
                .mapToInt(s -> s.getOnHandQty() != null ? s.getOnHandQty() : 0)
                .sum();
            esProduct.setPrice(price);
            esProduct.setSale(product.getSale());
            esProduct.setNewStatus(product.getNewStatus());
            esProduct.setRecommendStatus(product.getRecommendStatus());
            esProduct.setStock(stock);
            esProduct.setPromotionType(product.getPromotionType());
            // Load attribute values
            List<ProductAttributeValue> attrValues = attributeValueRepository.findByProductIdAndProductAttributeIdIn(
                    product.getId(), Collections.emptyList());
            // Actually load all for this product
            List<ProductAttributeValueDoc> esAttrValues = new ArrayList<>();
            // Use findAll and filter by product for simplicity
            try {
                attrValues = attributeValueRepository.findAll((root, query, cb) ->
                        cb.equal(root.get("productId"), product.getId()), Pageable.unpaged()).getContent();
            } catch (Exception e) {
                log.warn("Failed to load attribute values for product {}: {}", product.getId(), e.getMessage());
            }
            for (ProductAttributeValue attrValue : attrValues) {
                ProductAttributeValueDoc esAttrValue = new ProductAttributeValueDoc();
                esAttrValue.setId(attrValue.getId());
                esAttrValue.setProductAttributeId(attrValue.getProductAttributeId());
                esAttrValue.setValue(attrValue.getValue());
                esAttrValues.add(esAttrValue);
            }
            esProduct.setAttrValueList(esAttrValues);
            result.add(esProduct);
        }
        return result;
    }
}
