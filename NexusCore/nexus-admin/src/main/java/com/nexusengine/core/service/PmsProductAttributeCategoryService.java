package com.nexusengine.core.service;

import com.nexusengine.core.dto.PmsProductAttributeCategoryItem;
import com.nexusengine.core.model.PmsProductAttributeCategory;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/26.
 */
public interface PmsProductAttributeCategoryService {
    /**
     * Auto-generated documentation
     */
    int create(String name);

    /**
     * Auto-generated documentation
     */
    int update(Long id, String name);

    /**
     * Auto-generated documentation
     */
    int delete(Long id);

    /**
     * Auto-generated documentation
     */
    PmsProductAttributeCategory getItem(Long id);

    /**
     * Auto-generated documentation
     */
    List<PmsProductAttributeCategory> getList(Integer pageSize, Integer pageNum);

    /**
     * Auto-generated documentation
     */
    List<PmsProductAttributeCategoryItem> getListWithAttr();
}
