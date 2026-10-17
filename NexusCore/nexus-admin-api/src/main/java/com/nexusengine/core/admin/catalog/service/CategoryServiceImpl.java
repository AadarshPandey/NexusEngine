package com.nexusengine.core.admin.catalog.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.catalog.entity.Category;
import com.nexusengine.core.domain.catalog.entity.CategoryAttribute;
import org.springframework.data.domain.Sort;

import com.nexusengine.core.domain.catalog.repository.CategoryAttributeRepository;
import com.nexusengine.core.domain.catalog.repository.CategoryRepository;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;


import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import com.nexusengine.core.admin.catalog.dto.CategoryParam;
import com.nexusengine.core.admin.catalog.dto.CategoryWithChildrenItem;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final CategoryAttributeRepository productCategoryAttributeRelationRepository;

    @Override
    public int create(CategoryParam categoryParam) {
        Category productCategory = new Category();
        productCategory.setProductCount(0);
        copyCategoryParam(categoryParam, productCategory);
        setCategoryLevel(productCategory);
        productCategoryRepository.save(productCategory);
        List<Long> productAttributeIdList = categoryParam.productAttributeIdList();
        if (productAttributeIdList != null && !productAttributeIdList.isEmpty()) {
            insertRelationList(productCategory.getId(), productAttributeIdList);
        }
        return 1;
    }

    private void copyCategoryParam(CategoryParam param, Category category) {
        category.setParentId(param.parentId() != null && param.parentId() == 0L ? null : param.parentId());
        category.setName(param.name());
        if (param.navStatus() != null) {
            category.setIsInNavigation(param.navStatus() == 1);
        }
        if (param.showStatus() != null) {
            category.setIsVisible(param.showStatus() == 1);
        }
        category.setIconUrl(param.icon());
    }

    private void insertRelationList(Long productCategoryId, List<Long> productAttributeIdList) {
        List<CategoryAttribute> relationList = new ArrayList<>();
        for (Long productAttrId : productAttributeIdList) {
            CategoryAttribute relation = new CategoryAttribute();
            relation.setProductAttributeId(productAttrId);
            relation.setProductCategoryId(productCategoryId);
            relationList.add(relation);
        }
        productCategoryAttributeRelationRepository.saveAll(relationList);
    }

    @Override
    public int update(Long id, CategoryParam categoryParam) {
        Category productCategory = new Category();
        productCategory.setId(id);
        copyCategoryParam(categoryParam, productCategory);
        setCategoryLevel(productCategory);
        productCategoryAttributeRelationRepository.deleteByProductCategoryId(id);
        if (categoryParam.productAttributeIdList() != null && !categoryParam.productAttributeIdList().isEmpty()) {
            insertRelationList(id, categoryParam.productAttributeIdList());
        }
        productCategoryRepository.save(productCategory);
        return 1;
    }

    @Override
    public org.springframework.data.domain.Page<Category> getList(Long parentId, Integer pageSize, Integer pageNum) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(pageNum > 0 ? pageNum - 1 : 0, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        Long effectiveParentId = (parentId != null && parentId == 0L) ? null : parentId;
        return productCategoryRepository.findByParentId(effectiveParentId, pageable);
    }

    @Override
    public int delete(Long id) {
        productCategoryRepository.deleteById(id);
        return 1;
    }

    @Override
    public Category getItem(Long id) {
        return productCategoryRepository.findById(id).orElse(null);
    }

    @Override
    public int updateNavStatus(List<Long> ids, Integer navStatus) {
        List<Category> categories = productCategoryRepository.findAllById(ids);
        for (Category category : categories) {
            category.setIsInNavigation(navStatus != null && navStatus == 1);
            productCategoryRepository.save(category);
        }
        return categories.size();
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        List<Category> categories = productCategoryRepository.findAllById(ids);
        for (Category category : categories) {
            category.setIsVisible(showStatus != null && showStatus == 1);
            productCategoryRepository.save(category);
        }
        return categories.size();
    }

    @Override
    public List<CategoryWithChildrenItem> listWithChildren() {

        List<Category> allCategories = productCategoryRepository.findAll();
        List<CategoryWithChildrenItem> result = new ArrayList<>();

        for (Category category : allCategories) {
            if (category.getParentId() == null || category.getParentId() == 0L) {
                List<Category> children = new ArrayList<>();
                for (Category child : allCategories) {
                    if (child.getParentId() != null && child.getParentId().equals(category.getId())) {
                        children.add(child);
                    }
                }

                CategoryWithChildrenItem item = new CategoryWithChildrenItem(category, children);
                result.add(item);
            }
        }
        return result;
    }

    private void setCategoryLevel(Category productCategory) {
        if (productCategory.getParentId() == null || productCategory.getParentId() == 0L) {
            productCategory.setParentId(null);
            productCategory.setDepth(0);
        } else {
            Category parentCategory = productCategoryRepository.findById(productCategory.getParentId()).orElse(null);
            if (parentCategory != null && parentCategory.getDepth() != null) {
                productCategory.setDepth(parentCategory.getDepth() + 1);
            } else {
                productCategory.setDepth(0);
            }
        }
    }
}
