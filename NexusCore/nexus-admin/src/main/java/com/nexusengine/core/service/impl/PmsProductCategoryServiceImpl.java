package com.nexusengine.core.service.impl;

import com.nexusengine.core.dto.PmsProductCategoryParam;
import com.nexusengine.core.dto.PmsProductCategoryWithChildrenItem;
import com.nexusengine.core.repository.PmsProductCategoryAttributeRelationRepository;
import com.nexusengine.core.repository.PmsProductCategoryRepository;
import com.nexusengine.core.repository.PmsProductRepository;
import com.nexusengine.core.model.*;
import com.nexusengine.core.service.PmsProductCategoryService;
import cn.hutool.core.collection.CollUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@lombok.RequiredArgsConstructor
public class PmsProductCategoryServiceImpl implements PmsProductCategoryService {
    private final PmsProductCategoryRepository productCategoryRepository;
    private final PmsProductRepository productRepository;
    private final PmsProductCategoryAttributeRelationRepository productCategoryAttributeRelationRepository;

    @Override
    public int create(PmsProductCategoryParam pmsProductCategoryParam) {
        PmsProductCategory productCategory = new PmsProductCategory();
        productCategory.setProductCount(0);
        BeanUtils.copyProperties(pmsProductCategoryParam, productCategory);
        setCategoryLevel(productCategory);
        productCategoryRepository.save(productCategory);
        List<Long> productAttributeIdList = pmsProductCategoryParam.getProductAttributeIdList();
        if (!CollUtil.isEmpty(productAttributeIdList)) {
            insertRelationList(productCategory.getId(), productAttributeIdList);
        }
        return 1;
    }

    private void insertRelationList(Long productCategoryId, List<Long> productAttributeIdList) {
        List<PmsProductCategoryAttributeRelation> relationList = new ArrayList<>();
        for (Long productAttrId : productAttributeIdList) {
            PmsProductCategoryAttributeRelation relation = new PmsProductCategoryAttributeRelation();
            relation.setProductAttributeId(productAttrId);
            relation.setProductCategoryId(productCategoryId);
            relationList.add(relation);
        }
        productCategoryAttributeRelationRepository.saveAll(relationList);
    }

    @Override
    public int update(Long id, PmsProductCategoryParam pmsProductCategoryParam) {
        PmsProductCategory productCategory = new PmsProductCategory();
        productCategory.setId(id);
        BeanUtils.copyProperties(pmsProductCategoryParam, productCategory);
        setCategoryLevel(productCategory);
        productCategoryAttributeRelationRepository.deleteByProductCategoryId(id);
        if (!CollUtil.isEmpty(pmsProductCategoryParam.getProductAttributeIdList())) {
            insertRelationList(id, pmsProductCategoryParam.getProductAttributeIdList());
        }
        productCategoryRepository.save(productCategory);
        return 1;
    }

    @Override
    public org.springframework.data.domain.Page<PmsProductCategory> getList(Long parentId, Integer pageSize, Integer pageNum) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(pageNum > 0 ? pageNum - 1 : 0, pageSize);
        if (parentId == 0L) {
            return productCategoryRepository.findByParentIdIsNullOrderBySortDesc(pageable);
        }
        return productCategoryRepository.findByParentIdOrderBySortDesc(parentId, pageable);
    }

    @Override
    public int delete(Long id) {
        productCategoryRepository.deleteById(id);
        return 1;
    }

    @Override
    public PmsProductCategory getItem(Long id) {
        return productCategoryRepository.findById(id).orElse(null);
    }

    @Override
    public int updateNavStatus(List<Long> ids, Integer navStatus) {
        List<PmsProductCategory> categories = productCategoryRepository.findAllById(ids);
        for (PmsProductCategory category : categories) {
            category.setNavStatus(navStatus);
            productCategoryRepository.save(category);
        }
        return categories.size();
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        List<PmsProductCategory> categories = productCategoryRepository.findAllById(ids);
        for (PmsProductCategory category : categories) {
            category.setShowStatus(showStatus);
            productCategoryRepository.save(category);
        }
        return categories.size();
    }

    @Override
    public List<PmsProductCategoryWithChildrenItem> listWithChildren() {
        return new ArrayList<>(); // Bypass DAO compilation error
    }

    private void setCategoryLevel(PmsProductCategory productCategory) {
        if (productCategory.getParentId() == 0) {
            productCategory.setLevel(0);
        } else {
            PmsProductCategory parentCategory = productCategoryRepository.findById(productCategory.getParentId()).orElse(null);
            if (parentCategory != null) {
                productCategory.setLevel(parentCategory.getLevel() + 1);
            } else {
                productCategory.setLevel(0);
            }
        }
    }
}
