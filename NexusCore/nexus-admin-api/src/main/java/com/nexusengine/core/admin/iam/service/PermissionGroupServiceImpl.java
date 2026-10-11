package com.nexusengine.core.admin.iam.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.iam.repository.PermissionGroupRepository;


import com.nexusengine.core.domain.iam.entity.PermissionGroup;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PermissionGroupServiceImpl implements PermissionGroupService {
    private final PermissionGroupRepository resourceCategoryRepository;

    @Override
    public List<PermissionGroup> listAll() {
        return resourceCategoryRepository.findAll(Sort.by(Sort.Direction.DESC, "sort"));
    }

    @Override
    public int create(PermissionGroup permissionGroup) {
        permissionGroup.setCreateTime(java.time.OffsetDateTime.now());
        resourceCategoryRepository.save(permissionGroup);
        return 1;
    }

    @Override
    public int update(Long id, PermissionGroup permissionGroup) {
        permissionGroup.setId(id);
        resourceCategoryRepository.save(permissionGroup);
        return 1;
    }

    @Override
    public int delete(Long id) {
        resourceCategoryRepository.deleteById(id);
        return 1;
    }
}
