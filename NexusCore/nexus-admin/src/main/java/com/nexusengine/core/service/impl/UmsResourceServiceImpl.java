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
public class UmsResourceServiceImpl implements UmsResourceService {
    @Autowired
    private UmsResourceRepository resourceRepository;
    @Autowired
    private UmsAdminCacheService adminCacheService;

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
    public List<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum) {
        return resourceRepository.findAll(PageRequest.of(pageNum, pageSize)).getContent();
    }

    @Override
    public List<UmsResource> listAll() {
        return resourceRepository.findAll();
    }
}
