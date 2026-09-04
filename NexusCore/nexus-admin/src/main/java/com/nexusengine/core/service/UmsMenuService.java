package com.nexusengine.core.service;

import com.nexusengine.core.dto.UmsMenuNode;
import com.nexusengine.core.model.UmsMenu;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2020/2/2.
 */
public interface UmsMenuService {
    /**
     * Auto-generated documentation
     */
    int create(UmsMenu umsMenu);

    /**
     * Auto-generated documentation
     */
    int update(Long id, UmsMenu umsMenu);

    /**
     * Auto-generated documentation
     */
    UmsMenu getItem(Long id);

    /**
     * Auto-generated documentation
     */
    int delete(Long id);

    /**
     * Auto-generated documentation
     */
    List<UmsMenu> list(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * Auto-generated documentation
     */
    List<UmsMenuNode> treeList();

    /**
     * Auto-generated documentation
     */
    int updateHidden(Long id, Integer hidden);
}
