package com.nexusengine.core.service;

import com.nexusengine.core.model.UmsResource;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2020/2/2.
 */
public interface UmsResourceService {
    /**
     * Auto-generated documentation
     */
    int create(UmsResource umsResource);

    /**
     * Auto-generated documentation
     */
    int update(Long id, UmsResource umsResource);

    /**
     * Auto-generated documentation
     */
    UmsResource getItem(Long id);

    /**
     * Auto-generated documentation
     */
    int delete(Long id);

    /**
     * Auto-generated documentation
     */
    List<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum);

    /**
     * Auto-generated documentation
     */
    List<UmsResource> listAll();
}
