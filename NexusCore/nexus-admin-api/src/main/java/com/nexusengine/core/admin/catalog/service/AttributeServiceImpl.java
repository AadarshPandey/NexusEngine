package com.nexusengine.core.admin.catalog.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.catalog.repository.CategoryAttributeRepository;

import com.nexusengine.core.admin.catalog.dto.AttributeInfo;
import com.nexusengine.core.admin.catalog.dto.AttributeParam;
import com.nexusengine.core.domain.catalog.entity.CategoryAttribute;

import com.nexusengine.core.domain.catalog.repository.ProductTypeRepository;
import com.nexusengine.core.domain.catalog.repository.AttributeDefinitionRepository;
import com.nexusengine.core.domain.catalog.entity.AttributeDefinition;
import com.nexusengine.core.domain.catalog.entity.ProductType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {
    private final AttributeDefinitionRepository productAttributeRepository;
    private final CategoryAttributeRepository relationRepository;
    private final ProductTypeRepository productAttributeCategoryRepository;

    @Override
    public org.springframework.data.domain.Page<AttributeDefinition> getList(Long cid, Integer type, Integer pageSize, Integer pageNum) {
        return productAttributeRepository.findByProductAttributeCategoryIdAndType(cid, type,
                PageRequest.of(pageNum > 0 ? pageNum - 1 : 0, pageSize, Sort.by(Sort.Direction.DESC, "id")));
    }

    @Override
    public int create(AttributeParam attributeParam) {
        AttributeDefinition attributeDefinition = new AttributeDefinition();
        BeanUtils.copyProperties(attributeParam, attributeDefinition);
        productAttributeRepository.save(attributeDefinition);
        ProductType category = productAttributeCategoryRepository
                .findById(attributeDefinition.getProductAttributeCategoryId()).orElse(null);
        if (category != null) {
            if (attributeDefinition.getType() == 0) {
                category.setAttributeCount(category.getAttributeCount() + 1);
            } else if (attributeDefinition.getType() == 1) {
                category.setParamCount(category.getParamCount() + 1);
            }
            productAttributeCategoryRepository.save(category);
        }
        return 1;
    }

    @Override
    public int update(Long id, AttributeParam productAttributeParam) {
        AttributeDefinition attributeDefinition = new AttributeDefinition();
        attributeDefinition.setId(id);
        BeanUtils.copyProperties(productAttributeParam, attributeDefinition);
        productAttributeRepository.save(attributeDefinition);
        return 1;
    }

    @Override
    public AttributeDefinition getItem(Long id) {
        return productAttributeRepository.findById(id).orElse(null);
    }

    @Override
    public int delete(List<Long> ids) {
        AttributeDefinition first = productAttributeRepository.findById(ids.get(0)).orElse(null);
        productAttributeRepository.deleteAllById(ids);
        if (first != null) {
            ProductType category = productAttributeCategoryRepository
                    .findById(first.getProductAttributeCategoryId()).orElse(null);
            if (category != null) {
                int count = ids.size();
                if (first.getType() == 0) {
                    category.setAttributeCount(Math.max(0, category.getAttributeCount() - count));
                } else if (first.getType() == 1) {
                    category.setParamCount(Math.max(0, category.getParamCount() - count));
                }
                productAttributeCategoryRepository.save(category);
            }
        }
        return ids.size();
    }

    @Override
    public List<AttributeInfo> getProductAttrInfo(Long productCategoryId) {
        
        List<CategoryAttribute> relations = relationRepository.findAll();
        List<AttributeInfo> result = new ArrayList<>();
        
        for (CategoryAttribute rel : relations) {
            if (rel.getProductCategoryId() != null && rel.getProductCategoryId().equals(productCategoryId)) {
                AttributeDefinition attr = productAttributeRepository.findById(rel.getProductAttributeId()).orElse(null);
                if (attr != null) {
                    AttributeInfo info = new AttributeInfo(attr.getId(), attr.getProductAttributeCategoryId());
                    result.add(info);
                }
            }
        }
        return result;
    }
}
