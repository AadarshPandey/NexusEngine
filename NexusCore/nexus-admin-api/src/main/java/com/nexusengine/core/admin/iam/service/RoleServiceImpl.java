package com.nexusengine.core.admin.iam.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.iam.entity.Role;
import com.nexusengine.core.domain.iam.repository.RoleRepository;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Role management Service Implementation
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public int create(Role role) {
        role.setCreateTime(java.time.OffsetDateTime.now());
        role.setAdminCount(0);
        role.setSort(0);
        roleRepository.save(role);
        return 1;
    }

    @Override
    public int update(Long id, Role role) {
        role.setId(id);
        roleRepository.save(role);
        return 1;
    }

    @Override
    public int delete(List<Long> ids) {
        roleRepository.deleteAllById(ids);
        return ids.size();
    }

    @Override
    public List<Role> list() {
        return roleRepository.findAll();
    }

    @Override
    public Page<Role> list(String keyword, Integer pageSize, Integer pageNum) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(pageNum > 0 ? pageNum - 1 : 0, pageSize, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "id"));
        if (org.springframework.util.StringUtils.hasText(keyword)) {
            return roleRepository.findAll((org.springframework.data.jpa.domain.Specification<Role>) (root, query, cb) -> {
                String pattern = "%" + keyword + "%";
                return cb.like(root.get("name"), pattern);
            }, pageable);
        }
        return roleRepository.findAll(pageable);
    }
}
