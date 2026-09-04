package com.nexusengine.core.service;

import com.nexusengine.core.dto.PmsProductCategoryParam;
import com.nexusengine.core.dto.PmsProductCategoryWithChildrenItem;
import com.nexusengine.core.model.PmsProductCategory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/26.
 */
public interface PmsProductCategoryService {
    /**
     * Auto-generated documentation
     */
    @Transactional
    int create(PmsProductCategoryParam pmsProductCategoryParam);

    /**
     * Auto-generated documentation
     */
    @Transactional
    int update(Long id, PmsProductCategoryParam pmsProductCategoryParam);

    /**
     * Auto-generated documentation
     */
    List<PmsProductCategory> getList(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * Auto-generated documentation
     */
    int delete(Long id);

    /**
     * Auto-generated documentation
     */
    PmsProductCategory getItem(Long id);

    /**
     * Auto-generated documentation
     */
    int updateNavStatus(List<Long> ids, Integer navStatus);

    /**
     * Auto-generated documentation
     */
    int updateShowStatus(List<Long> ids, Integer showStatus);

    /**
     * Auto-generated documentation
     */
    List<PmsProductCategoryWithChildrenItem> listWithChildren();
}
