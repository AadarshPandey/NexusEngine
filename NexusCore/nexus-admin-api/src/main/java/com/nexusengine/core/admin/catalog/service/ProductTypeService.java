package com.nexusengine.core.admin.catalog.service;

import com.nexusengine.core.domain.catalog.entity.ProductType;

import com.nexusengine.core.admin.catalog.dto.ProductTypeWithAttributes;

import java.util.List;


/**
 * Represents the ProductTypeService component.
 * Provides core functionality and operations for ProductTypeService.
 */
public interface ProductTypeService {
        /**
     * Executes the operation.
     * @param name the name
     * @return the result of the operation
     */
    int create(String name);

        /**
     * Executes the operation.
     * @param id the id
     * @param name the name
     * @return the result of the operation
     */
    int update(Long id, String name);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    int delete(Long id);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    ProductType getItem(Long id);

        /**
     * Executes the operation.
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    org.springframework.data.domain.Page<ProductType> getList(Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<ProductTypeWithAttributes> getListWithAttr();
}
