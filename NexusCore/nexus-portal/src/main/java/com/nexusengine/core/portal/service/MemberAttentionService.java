package com.nexusengine.core.portal.service;

import com.nexusengine.core.portal.domain.MemberBrandAttention;
import org.springframework.data.domain.Page;

/**
 * Represents the MemberAttentionService component.
 * Provides core functionality and operations for MemberAttentionService.
 */
public interface MemberAttentionService {
        /**
     * Executes the operation.
     * @param memberBrandAttention the memberBrandAttention
     * @return the result of the operation
     */
    int add(MemberBrandAttention memberBrandAttention);

        /**
     * Executes the operation.
     * @param brandId the brandId
     * @return the result of the operation
     */
    int delete(Long brandId);

        /**
     * Executes the operation.
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    Page<MemberBrandAttention> list(Integer pageNum, Integer pageSize);

        /**
     * Executes the operation.
     * @param brandId the brandId
     * @return the result of the operation
     */
    MemberBrandAttention detail(Long brandId);

        /**
     * Executes the operation.
     */
    void clear();
}
