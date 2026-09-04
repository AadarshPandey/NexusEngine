package com.nexusengine.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.repository.UmsRoleRepository;
import com.nexusengine.core.repository.UmsRoleMenuRelationRepository;
import com.nexusengine.core.repository.UmsRoleResourceRelationRepository;
import com.nexusengine.core.model.*;
import com.nexusengine.core.service.UmsAdminCacheService;
import com.nexusengine.core.service.UmsRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Role management Service Implementation
 */
@Service
public class UmsRoleServiceImpl implements UmsRoleService {
    @Autowired
    private UmsRoleRepository roleRepository;
    @Autowired
    private UmsRoleMenuRelationRepository roleMenuRelationRepository;
    @Autowired
    private UmsRoleResourceRelationRepository roleResourceRelationRepository;
    @Autowired
    private UmsAdminCacheService adminCacheService;

    @Override
    public int create(UmsRole role) {
        role.setCreateTime(new Date());
        role.setAdminCount(0);
        role.setSort(0);
        roleRepository.save(role);
        return 1;
    }

    @Override
    public int update(Long id, UmsRole role) {
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
    public List<UmsRole> list() {
        return roleRepository.findAll();
    }

    @Override
    public List<UmsRole> list(String keyword, Integer pageSize, Integer pageNum) {
        return roleRepository.findAll(PageRequest.of(pageNum, pageSize)).getContent();
    }

    @Override
    public List<UmsMenu> getMenuList(Long adminId) {
        return roleRepository.getMenuList(adminId);
    }

    @Override
    public List<UmsMenu> listMenu(Long roleId) {
        return roleRepository.getMenuListByRoleId(roleId);
    }

    @Override
    public List<UmsResource> listResource(Long roleId) {
        return roleRepository.getResourceListByRoleId(roleId);
    }

    @Override
    public int allocMenu(Long roleId, List<Long> menuIds) {
        roleMenuRelationRepository.deleteByRoleId(roleId);
        for (Long menuId : menuIds) {
            UmsRoleMenuRelation relation = new UmsRoleMenuRelation();
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
            UmsRoleResourceRelation relation = new UmsRoleResourceRelation();
            relation.setRoleId(roleId);
            relation.setResourceId(resourceId);
            roleResourceRelationRepository.save(relation);
        }
        adminCacheService.delResourceListByRole(roleId);
        return resourceIds.size();
    }
}
