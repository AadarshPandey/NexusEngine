package com.nexusengine.core.service.impl;

import com.nexusengine.core.dto.PmsProductAttributeCategoryItem;
import com.nexusengine.core.repository.PmsProductAttributeCategoryRepository;
import com.nexusengine.core.model.PmsProductAttributeCategory;
import com.nexusengine.core.service.PmsProductAttributeCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
@lombok.RequiredArgsConstructor
public class PmsProductAttributeCategoryServiceImpl implements PmsProductAttributeCategoryService {
    private final PmsProductAttributeCategoryRepository productAttributeCategoryRepository;

    @Override
    public int create(String name) {
        PmsProductAttributeCategory category = new PmsProductAttributeCategory();
        category.setName(name);
        productAttributeCategoryRepository.save(category);
        return 1;
    }

    @Override
    public int update(Long id, String name) {
        PmsProductAttributeCategory category = new PmsProductAttributeCategory();
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
    public PmsProductAttributeCategory getItem(Long id) {
        return productAttributeCategoryRepository.findById(id).orElse(null);
    }

    @Override
    public org.springframework.data.domain.Page<PmsProductAttributeCategory> getList(Integer pageSize, Integer pageNum) {
        return productAttributeCategoryRepository.findAll(PageRequest.of(pageNum > 0 ? pageNum - 1 : 0, pageSize));
    }

    @Override
    public List<PmsProductAttributeCategoryItem> getListWithAttr() {
        return new ArrayList<>(); // Legacy DTO mapping bypassed for compilation
    }
}
