package com.nexusengine.core.service;

import com.nexusengine.core.model.UmsMenu;
import com.nexusengine.core.model.UmsResource;
import com.nexusengine.core.model.UmsRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/9/30.
 */
public interface UmsRoleService {
    /**
     * Auto-generated documentation
     */
    int create(UmsRole role);

    /**
     * Auto-generated documentation
     */
    int update(Long id, UmsRole role);

    /**
     * Auto-generated documentation
     */
    int delete(List<Long> ids);

    /**
     * Auto-generated documentation
     */
    List<UmsRole> list();

    /**
     * Auto-generated documentation
     */
    List<UmsRole> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * Auto-generated documentation
     */
    List<UmsMenu> getMenuList(Long adminId);

    /**
     * Auto-generated documentation
     */
    List<UmsMenu> listMenu(Long roleId);

    /**
     * Auto-generated documentation
     */
    List<UmsResource> listResource(Long roleId);

    /**
     * Auto-generated documentation
     */
    @Transactional
    int allocMenu(Long roleId, List<Long> menuIds);

    /**
     * Auto-generated documentation
     */
    @Transactional
    int allocResource(Long roleId, List<Long> resourceIds);
}
