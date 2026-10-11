package com.nexusengine.core.admin.catalog.service;
import com.nexusengine.core.domain.catalog.entity.ProductPriceChange;

import com.nexusengine.core.admin.catalog.dto.ProductParam;
import com.nexusengine.core.admin.catalog.dto.ProductQueryParam;
import com.nexusengine.core.admin.catalog.dto.ProductResult;
import com.nexusengine.core.domain.catalog.entity.ProductApprovalEvent;

import com.nexusengine.core.domain.catalog.entity.Product;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Auto-generated documentation
 * Created by macro on 2018/4/26.
 */
public interface ProductService {
        /**
     * Executes the operation.
     * @param productParam the productParam
     * @return the result of the operation
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED)
    int create(ProductParam productParam);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    ProductResult getUpdateInfo(Long id);

        /**
     * Executes the operation.
     * @param id the id
     * @param productParam the productParam
     * @return the result of the operation
     */
    @Transactional
    int update(Long id, ProductParam productParam);

        /**
     * Executes the operation.
     * @param productQueryParam the productQueryParam
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    org.springframework.data.domain.Page<Product> list(ProductQueryParam productQueryParam, Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @param ids the ids
     * @param verifyStatus the verifyStatus
     * @param detail the detail
     * @return the result of the operation
     */
    @Transactional
    int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail);
    List<ProductPriceChange> getAuditLog(Long id);
    List<ProductApprovalEvent> getApprovalHistory(Long id);

        /**
     * Executes the operation.
     * @param ids the ids
     * @param publishStatus the publishStatus
     * @return the result of the operation
     */
    int updatePublishStatus(List<Long> ids, Integer publishStatus);

        /**
     * Executes the operation.
     * @param ids the ids
     * @param recommendStatus the recommendStatus
     * @return the result of the operation
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

        /**
     * Executes the operation.
     * @param ids the ids
     * @param newStatus the newStatus
     * @return the result of the operation
     */
    int updateNewStatus(List<Long> ids, Integer newStatus);

        /**
     * Executes the operation.
     * @param ids the ids
     * @param deleteStatus the deleteStatus
     * @return the result of the operation
     */
    int updateDeleteStatus(List<Long> ids, Integer deleteStatus);

        /**
     * Executes the operation.
     * @param keyword the keyword
     * @return the result of the operation
     */
    List<Product> list(String keyword);
}
