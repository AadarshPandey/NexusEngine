package com.nexusengine.core.admin.catalog.service;

import com.nexusengine.core.domain.catalog.entity.Brand;

import com.nexusengine.core.admin.catalog.dto.BrandParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Auto-generated documentation
 * Created by macro on 2018/4/26.
 */
public interface BrandService {
        /**
     * Executes the operation.
     * @return the result of the operation
     */
    org.springframework.data.domain.Page<Brand> listAllBrand();

        /**
     * Executes the operation.
     * @param brandParam the brandParam
     * @return the result of the operation
     */
    int createBrand(BrandParam brandParam);

        /**
     * Executes the operation.
     * @param id the id
     * @param brandParam the brandParam
     * @return the result of the operation
     */
    @Transactional
    int updateBrand(Long id, BrandParam brandParam);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    int deleteBrand(Long id);

        /**
     * Executes the operation.
     * @param ids the ids
     * @return the result of the operation
     */
    int deleteBrand(List<Long> ids);

        /**
     * Executes the operation.
     * @param keyword the keyword
     * @param isVisible the isVisible
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    org.springframework.data.domain.Page<Brand> listBrand(String keyword, Integer isVisible, int pageNum, int pageSize);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    Brand getBrand(Long id);

        /**
     * Executes the operation.
     * @param ids the ids
     * @param isVisible the isVisible
     * @return the result of the operation
     */
    int updateIsVisible(List<Long> ids, Integer isVisible);
}
