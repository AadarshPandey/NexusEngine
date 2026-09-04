package com.nexusengine.core.search.dao;

import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.PmsProductAttributeValue;
import com.nexusengine.core.repository.PmsProductAttributeValueRepository;
import com.nexusengine.core.repository.PmsProductRepository;
import com.nexusengine.core.search.domain.EsProduct;
import com.nexusengine.core.search.domain.EsProductAttributeValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Search product data access - converts JPA product data to ES product format
 */
@Repository
public class EsProductDao {
    @Autowired
    private PmsProductRepository productRepository;
    @Autowired
    private PmsProductAttributeValueRepository attributeValueRepository;

    /**
     * Get all products formatted for Elasticsearch indexing
     * @param id if provided, returns only the product with this id; otherwise returns all
     */
    public List<EsProduct> getAllEsProductList(Long id) {
        List<PmsProduct> products;
        if (id != null) {
            products = productRepository.findAllById(Collections.singletonList(id));
        } else {
            products = productRepository.findAll();
        }
        List<EsProduct> result = new ArrayList<>();
        for (PmsProduct product : products) {
            EsProduct esProduct = new EsProduct();
            esProduct.setId(product.getId());
            esProduct.setProductSn(product.getProductSn());
            esProduct.setBrandId(product.getBrandId());
            esProduct.setBrandName(product.getBrandName());
            esProduct.setProductCategoryId(product.getProductCategoryId());
            esProduct.setProductCategoryName(product.getProductCategoryName());
            esProduct.setPic(product.getPic());
            esProduct.setName(product.getName());
            esProduct.setSubTitle(product.getSubTitle());
            esProduct.setKeywords(product.getKeywords());
            esProduct.setPrice(product.getPrice());
            esProduct.setSale(product.getSale());
            esProduct.setNewStatus(product.getNewStatus());
            esProduct.setRecommandStatus(product.getRecommandStatus());
            esProduct.setStock(product.getStock());
            esProduct.setPromotionType(product.getPromotionType());
            esProduct.setSort(product.getSort());
            // Load attribute values
            List<PmsProductAttributeValue> attrValues = attributeValueRepository.findByProductIdAndProductAttributeIdIn(
                    product.getId(), Collections.emptyList());
            // Actually load all for this product
            List<EsProductAttributeValue> esAttrValues = new ArrayList<>();
            // Use findAll and filter by product for simplicity
            try {
                attrValues = attributeValueRepository.findAll((root, query, cb) ->
                        cb.equal(root.get("productId"), product.getId()), org.springframework.data.domain.Pageable.unpaged()).getContent();
            } catch (Exception e) {
                // Fallback
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
