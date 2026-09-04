package com.nexusengine.core.service;

import com.nexusengine.core.model.UmsResourceCategory;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2020/2/5.
 */
public interface UmsResourceCategoryService {

    /**
     * Auto-generated documentation
     */
    List<UmsResourceCategory> listAll();

    /**
     * Auto-generated documentation
     */
    int create(UmsResourceCategory umsResourceCategory);

    /**
     * Auto-generated documentation
     */
    int update(Long id, UmsResourceCategory umsResourceCategory);

    /**
     * Auto-generated documentation
     */
    int delete(Long id);
}
