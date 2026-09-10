package com.nexusengine.core.service;

import com.nexusengine.core.dto.PmsProductAttributeParam;
import com.nexusengine.core.dto.ProductAttrInfo;
import com.nexusengine.core.model.PmsProductAttribute;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Represents the PmsProductAttributeService component.
 * Provides core functionality and operations for PmsProductAttributeService.
 */
public interface PmsProductAttributeService {
        /**
     * Executes the operation.
     * @param cid the cid
     * @param type the type
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    List<PmsProductAttribute> getList(Long cid, Integer type, Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @param pmsProductAttributeParam the pmsProductAttributeParam
     * @return the result of the operation
     */
    @Transactional
    int create(PmsProductAttributeParam pmsProductAttributeParam);

        /**
     * Executes the operation.
     * @param id the id
     * @param productAttributeParam the productAttributeParam
     * @return the result of the operation
     */
    int update(Long id, PmsProductAttributeParam productAttributeParam);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    PmsProductAttribute getItem(Long id);

        /**
     * Executes the operation.
     * @param ids the ids
     * @return the result of the operation
     */
    @Transactional
    int delete(List<Long> ids);

        /**
     * Executes the operation.
     * @param productCategoryId the productCategoryId
     * @return the result of the operation
     */
    List<ProductAttrInfo> getProductAttrInfo(Long productCategoryId);
}
