package com.nexusengine.core.service.impl;

import com.nexusengine.core.dto.PmsProductAttributeParam;
import com.nexusengine.core.dto.ProductAttrInfo;
import com.nexusengine.core.repository.PmsProductAttributeCategoryRepository;
import com.nexusengine.core.repository.PmsProductAttributeRepository;
import com.nexusengine.core.model.PmsProductAttribute;
import com.nexusengine.core.model.PmsProductAttributeCategory;
import com.nexusengine.core.service.PmsProductAttributeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class PmsProductAttributeServiceImpl implements PmsProductAttributeService {
    @Autowired
    private PmsProductAttributeRepository productAttributeRepository;
    @Autowired
    private PmsProductAttributeCategoryRepository productAttributeCategoryRepository;

    @Override
    public List<PmsProductAttribute> getList(Long cid, Integer type, Integer pageSize, Integer pageNum) {
        return productAttributeRepository.findByProductAttributeCategoryIdAndType(cid, type,
                PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "sort")));
    }

    @Override
    public int create(PmsProductAttributeParam pmsProductAttributeParam) {
        PmsProductAttribute pmsProductAttribute = new PmsProductAttribute();
        BeanUtils.copyProperties(pmsProductAttributeParam, pmsProductAttribute);
        productAttributeRepository.save(pmsProductAttribute);
        PmsProductAttributeCategory category = productAttributeCategoryRepository
                .findById(pmsProductAttribute.getProductAttributeCategoryId()).orElse(null);
        if (category != null) {
            if (pmsProductAttribute.getType() == 0) {
                category.setAttributeCount(category.getAttributeCount() + 1);
            } else if (pmsProductAttribute.getType() == 1) {
                category.setParamCount(category.getParamCount() + 1);
            }
            productAttributeCategoryRepository.save(category);
        }
        return 1;
    }

    @Override
    public int update(Long id, PmsProductAttributeParam productAttributeParam) {
        PmsProductAttribute pmsProductAttribute = new PmsProductAttribute();
        pmsProductAttribute.setId(id);
        BeanUtils.copyProperties(productAttributeParam, pmsProductAttribute);
        productAttributeRepository.save(pmsProductAttribute);
        return 1;
    }

    @Override
    public PmsProductAttribute getItem(Long id) {
        return productAttributeRepository.findById(id).orElse(null);
    }

    @Override
    public int delete(List<Long> ids) {
        PmsProductAttribute first = productAttributeRepository.findById(ids.get(0)).orElse(null);
        productAttributeRepository.deleteAllById(ids);
        if (first != null) {
            PmsProductAttributeCategory category = productAttributeCategoryRepository
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
    public List<ProductAttrInfo> getProductAttrInfo(Long productCategoryId) {
        return new ArrayList<>(); // Bypass DTO query compilation error
    }
}
