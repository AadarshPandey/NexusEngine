package com.nexusengine.core.admin.iam.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.iam.entity.ConsoleMenu;
import com.nexusengine.core.admin.catalog.dto.*;
import com.nexusengine.core.admin.catalog.service.*;
import com.nexusengine.core.admin.analytics.service.*;
import com.nexusengine.core.admin.ordering.dto.*;
import com.nexusengine.core.admin.ordering.service.*;
import com.nexusengine.core.admin.promotion.dto.*;
import com.nexusengine.core.admin.promotion.service.*;
import com.nexusengine.core.admin.ai.service.*;
import com.nexusengine.core.admin.infra.dto.*;
import com.nexusengine.core.admin.infra.service.*;
import com.nexusengine.core.admin.iam.dto.*;
import com.nexusengine.core.admin.iam.service.*;
import com.nexusengine.core.domain.iam.entity.Role;
import com.nexusengine.core.domain.iam.entity.RoleMenu;
import com.nexusengine.core.domain.iam.entity.Permission;
import com.nexusengine.core.domain.iam.entity.RolePermission;

import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.domain.iam.repository.RoleRepository;
import com.nexusengine.core.domain.iam.repository.RoleMenuRepository;
import com.nexusengine.core.domain.iam.repository.RolePermissionRepository;

import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Role management Service Implementation
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMenuRepository roleMenuRelationRepository;
    private final RolePermissionRepository roleResourceRelationRepository;
    private final AdminUserCacheService adminCacheService;

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
        adminCacheService.delResourceListByRoleIds(ids);
        return ids.size();
    }

    @Override
    public List<Role> list() {
        return /* findAll() is acceptable for small reference tables */
        roleRepository.findAll();
    }

    @Override
    public Page<Role> list(String keyword, Integer pageSize, Integer pageNum) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(pageNum > 0 ? pageNum - 1 : 0, pageSize, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "id"));
        if (cn.hutool.core.util.StrUtil.isNotEmpty(keyword)) {
            return roleRepository.findAll((org.springframework.data.jpa.domain.Specification<Role>) (root, query, cb) -> {
                String pattern = "%" + keyword + "%";
                return cb.like(root.get("name"), pattern);
            }, pageable);
        }
        return roleRepository.findAll(pageable);
    }

    @Override
    public List<ConsoleMenu> getMenuList(Long adminId) {
        return roleRepository.getMenuList(adminId);
    }

    @Override
    public List<ConsoleMenu> listMenu(Long roleId) {
        return roleRepository.getMenuListByRoleId(roleId);
    }

    @Override
    public List<Permission> listResource(Long roleId) {
        return roleRepository.getResourceListByRoleId(roleId);
    }

    @Override
    public int allocMenu(Long roleId, List<Long> menuIds) {
        roleMenuRelationRepository.deleteByRoleId(roleId);
        for (Long menuId : menuIds) {
            RoleMenu relation = new RoleMenu();
            relation.setRoleId(roleId);
            relation.setMenuId(menuId);
            roleMenuRelationRepository.save(relation);
        }
        return menuIds.size();
    }

    @Override
    public int allocResource(Long roleId, List<Long> resourceIds) {
        roleResourceRelationRepository.deleteByRoleId(roleId);
        for (Long resourceId : resourceIds) {
            RolePermission relation = new RolePermission();
            relation.setRoleId(roleId);
            relation.setResourceId(resourceId);
            roleResourceRelationRepository.save(relation);
        }
        adminCacheService.delResourceListByRole(roleId);
        return resourceIds.size();
    }
}
