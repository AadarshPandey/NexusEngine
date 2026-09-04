package com.nexusengine.core.service;

import com.nexusengine.core.dto.PmsProductAttributeParam;
import com.nexusengine.core.dto.ProductAttrInfo;
import com.nexusengine.core.model.PmsProductAttribute;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/26.
 */
public interface PmsProductAttributeService {
    /**
     * Auto-generated documentation
     * Auto-generated documentation
     * Auto-generated documentation
     */
    List<PmsProductAttribute> getList(Long cid, Integer type, Integer pageSize, Integer pageNum);

    /**
     * Auto-generated documentation
     */
    @Transactional
    int create(PmsProductAttributeParam pmsProductAttributeParam);

    /**
     * Auto-generated documentation
     */
    int update(Long id, PmsProductAttributeParam productAttributeParam);

    /**
     * Auto-generated documentation
     */
    PmsProductAttribute getItem(Long id);

    /**
     * Auto-generated documentation
     */
    @Transactional
    int delete(List<Long> ids);

    /**
     * Auto-generated documentation
     */
    List<ProductAttrInfo> getProductAttrInfo(Long productCategoryId);
}
