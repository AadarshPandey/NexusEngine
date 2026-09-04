package com.nexusengine.core.service.impl;

import com.nexusengine.core.repository.UmsResourceCategoryRepository;
import com.nexusengine.core.model.UmsResourceCategory;
import com.nexusengine.core.service.UmsResourceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UmsResourceCategoryServiceImpl implements UmsResourceCategoryService {
    @Autowired
    private UmsResourceCategoryRepository resourceCategoryRepository;

    @Override
    public List<UmsResourceCategory> listAll() {
        return resourceCategoryRepository.findAll(Sort.by(Sort.Direction.DESC, "sort"));
    }

    @Override
    public int create(UmsResourceCategory umsResourceCategory) {
        umsResourceCategory.setCreateTime(new Date());
        resourceCategoryRepository.save(umsResourceCategory);
        return 1;
    }

    @Override
    public int update(Long id, UmsResourceCategory umsResourceCategory) {
        umsResourceCategory.setId(id);
        resourceCategoryRepository.save(umsResourceCategory);
        return 1;
    }

    @Override
    public int delete(Long id) {
        resourceCategoryRepository.deleteById(id);
        return 1;
    }
}
