package com.nexusengine.core.admin.iam.service;

import com.nexusengine.core.domain.iam.entity.Role;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleService {
    int create(Role role);

    int update(Long id, Role role);

    int delete(List<Long> ids);

    List<Role> list();

    Page<Role> list(String keyword, Integer pageSize, Integer pageNum);
}
