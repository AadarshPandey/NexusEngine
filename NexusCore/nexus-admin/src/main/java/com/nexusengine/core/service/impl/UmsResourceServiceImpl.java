package com.nexusengine.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.repository.UmsResourceRepository;
import com.nexusengine.core.model.UmsResource;
import com.nexusengine.core.service.UmsAdminCacheService;
import com.nexusengine.core.service.UmsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@lombok.RequiredArgsConstructor
public class UmsResourceServiceImpl implements UmsResourceService {
    private final UmsResourceRepository resourceRepository;
    private final UmsAdminCacheService adminCacheService;

    @Override
    public int create(UmsResource umsResource) {
        umsResource.setCreateTime(new Date());
        resourceRepository.save(umsResource);
        return 1;
    }

    @Override
    public int update(Long id, UmsResource umsResource) {
        umsResource.setId(id);
        resourceRepository.save(umsResource);
        adminCacheService.delResourceListByResource(id);
        return 1;
    }

    @Override
    public UmsResource getItem(Long id) {
        return resourceRepository.findById(id).orElse(null);
    }

    @Override
    public int delete(Long id) {
        resourceRepository.deleteById(id);
        adminCacheService.delResourceListByResource(id);
        return 1;
    }

    @Override
    public org.springframework.data.domain.Page<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum) {
        int page = pageNum > 0 ? pageNum - 1 : 0;
        return resourceRepository.findAll((org.springframework.data.jpa.domain.Specification<UmsResource>) (root, query, cb) -> {
            java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("categoryId"), categoryId));
            }
            if (StrUtil.isNotEmpty(nameKeyword)) {
                predicates.add(cb.like(root.get("name"), "%" + nameKeyword + "%"));
            }
            if (StrUtil.isNotEmpty(urlKeyword)) {
                predicates.add(cb.like(root.get("url"), "%" + urlKeyword + "%"));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, PageRequest.of(page, pageSize));
    }

    @Override
    public List<UmsResource> listAll() {
        return /* findAll() is acceptable for small reference tables */
        resourceRepository.findAll();
    }
}
