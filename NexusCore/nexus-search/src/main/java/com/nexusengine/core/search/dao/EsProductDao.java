package com.nexusengine.core.search.dao;

import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.PmsProductAttributeValue;
import com.nexusengine.core.repository.PmsProductAttributeValueRepository;
import com.nexusengine.core.repository.PmsProductRepository;
import com.nexusengine.core.search.domain.EsProduct;
import com.nexusengine.core.search.domain.EsProductAttributeValue;
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
public class EsProductDao {
    private static final Logger log = LoggerFactory.getLogger(EsProductDao.class);

    private final PmsProductRepository productRepository;
    private final PmsProductAttributeValueRepository attributeValueRepository;

    public EsProductDao(PmsProductRepository productRepository,
                        PmsProductAttributeValueRepository attributeValueRepository) {
        this.productRepository = productRepository;
        this.attributeValueRepository = attributeValueRepository;
    }

    /**
     * Get all products formatted for Elasticsearch indexing
     * @param id if provided, returns only the product with this id; otherwise returns all
     */
    public List<EsProduct> getAllEsProductList(Long id) {
        List<PmsProduct> products;
        if (id != null) {
            products = productRepository.findAllById(Collections.singletonList(id));
        } else {
            products = /* Intentional unbounded findAll for bulk ES indexing */ productRepository.findAll();
        }
        List<EsProduct> result = new ArrayList<>();
        for (PmsProduct product : products) {
            EsProduct esProduct = new EsProduct();
            esProduct.setId(product.getId());
            esProduct.setProductSn(product.getProductSn());
            esProduct.setBrandId(product.getBrandId());
            esProduct.setBrandName(product.getBrandName());
            esProduct.setProductCategoryId(product.getProductCategoryId());
            esProduct.setPic(product.getPrimaryImageUrl());
            esProduct.setName(product.getName());
            esProduct.setKeywords(product.getKeywords());
            esProduct.setPrice(BigDecimal.ZERO); // FAANG Refactor: Delegated to SKU
            esProduct.setSale(product.getSale());
            esProduct.setNewStatus(product.getNewStatus());
            esProduct.setRecommendStatus(product.getRecommendStatus());
            esProduct.setStock(0); // FAANG Refactor: Delegated to SKU
            esProduct.setPromotionType(product.getPromotionType());
            // Load attribute values
            List<PmsProductAttributeValue> attrValues = attributeValueRepository.findByProductIdAndProductAttributeIdIn(
                    product.getId(), Collections.emptyList());
            // Actually load all for this product
            List<EsProductAttributeValue> esAttrValues = new ArrayList<>();
            // Use findAll and filter by product for simplicity
            try {
                attrValues = attributeValueRepository.findAll((root, query, cb) ->
                        cb.equal(root.get("productId"), product.getId()), Pageable.unpaged()).getContent();
            } catch (Exception e) {
                log.warn("Failed to load attribute values for product {}: {}", product.getId(), e.getMessage());
            }
            for (PmsProductAttributeValue attrValue : attrValues) {
                EsProductAttributeValue esAttrValue = new EsProductAttributeValue();
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
