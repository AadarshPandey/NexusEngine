package com.nexusengine.core.admin.catalog.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.catalog.entity.AttributeDefinition;

import com.nexusengine.core.admin.catalog.dto.ProductTypeWithAttributes;
import com.nexusengine.core.domain.catalog.repository.AttributeDefinitionRepository;

import com.nexusengine.core.domain.catalog.repository.ProductTypeRepository;
import com.nexusengine.core.domain.catalog.entity.ProductType;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {
    private final ProductTypeRepository productAttributeCategoryRepository;
    private final AttributeDefinitionRepository productAttributeRepository;

    @Override
    public int create(String name) {
        ProductType category = new ProductType();
        category.setName(name);
        productAttributeCategoryRepository.save(category);
        return 1;
    }

    @Override
    public int update(Long id, String name) {
        ProductType category = new ProductType();
        category.setName(name);
        category.setId(id);
        productAttributeCategoryRepository.save(category);
        return 1;
    }

    @Override
    public int delete(Long id) {
        productAttributeCategoryRepository.deleteById(id);
        return 1;
    }

    @Override
    public ProductType getItem(Long id) {
        return productAttributeCategoryRepository.findById(id).orElse(null);
    }

    @Override
    public org.springframework.data.domain.Page<ProductType> getList(Integer pageSize, Integer pageNum) {
        return productAttributeCategoryRepository.findAll(PageRequest.of(pageNum > 0 ? pageNum - 1 : 0, pageSize));
    }

    @Override
    public List<ProductTypeWithAttributes> getListWithAttr() {

        List<ProductType> allCategories = productAttributeCategoryRepository.findAll();
        List<ProductTypeWithAttributes> result = new ArrayList<>();

        for (ProductType category : allCategories) {
            // fetch attributes
            List<AttributeDefinition> attrs = productAttributeRepository.findByProductAttributeCategoryId(category.getId());

            ProductTypeWithAttributes item = new ProductTypeWithAttributes(category, attrs);
            result.add(item);
        }
        return result;
    }
}
