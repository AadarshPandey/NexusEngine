package com.nexusengine.core.search.service;

import com.nexusengine.core.search.domain.EsProduct;
import com.nexusengine.core.search.domain.EsProductRelatedInfo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Represents the EsProductService component.
 * Provides core functionality and operations for EsProductService.
 */
public interface EsProductService {
        /**
     * Executes the operation.
     * @return the result of the operation
     */
    int importAll();

        /**
     * Executes the operation.
     * @param id the id
     */
    void delete(Long id);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    EsProduct create(Long id);

        /**
     * Executes the operation.
     * @param ids the ids
     */
    void delete(List<Long> ids);

        /**
     * Executes the operation.
     * @param keyword the keyword
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize);

        /**
     * Executes the operation.
     * @param keyword the keyword
     * @param brandId the brandId
     * @param productCategoryId the productCategoryId
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @param sort the sort
     * @return the result of the operation
     */
    Page<EsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize,Integer sort);

        /**
     * Executes the operation.
     * @param id the id
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    Page<EsProduct> recommend(Long id, Integer pageNum, Integer pageSize);

        /**
     * Executes the operation.
     * @param keyword the keyword
     * @return the result of the operation
     */
    EsProductRelatedInfo searchRelatedInfo(String keyword);
}
