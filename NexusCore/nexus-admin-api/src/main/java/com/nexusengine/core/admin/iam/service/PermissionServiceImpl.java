package com.nexusengine.core.admin.iam.service;

import lombok.RequiredArgsConstructor;

import cn.hutool.core.util.StrUtil;


import com.nexusengine.core.domain.iam.repository.PermissionRepository;
import com.nexusengine.core.domain.iam.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository resourceRepository;
    private final AdminUserCacheService adminCacheService;

    @Override
    public int create(Permission permission) {
        permission.setCreateTime(java.time.OffsetDateTime.now());
        resourceRepository.save(permission);
        return 1;
    }

    @Override
    public int update(Long id, Permission permission) {
        permission.setId(id);
        resourceRepository.save(permission);
        adminCacheService.delResourceListByResource(id);
        return 1;
    }

    @Override
    public Permission getItem(Long id) {
        return resourceRepository.findById(id).orElse(null);
    }

    @Override
    public int delete(Long id) {
        resourceRepository.deleteById(id);
        adminCacheService.delResourceListByResource(id);
        return 1;
    }

    @Override
    public org.springframework.data.domain.Page<Permission> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum) {
        int page = pageNum > 0 ? pageNum - 1 : 0;
        return resourceRepository.findAll((org.springframework.data.jpa.domain.Specification<Permission>) (root, query, cb) -> {
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
        }, PageRequest.of(page, pageSize, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "id")));
    }

    @Override
    public List<Permission> listAll() {
        return /* findAll() is acceptable for small reference tables */
        resourceRepository.findAll();
    }
}
