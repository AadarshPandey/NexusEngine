package com.nexusengine.core.portal.service;

import com.nexusengine.core.portal.domain.MemberProductCollection;
import org.springframework.data.domain.Page;

/**
 * Represents the MemberCollectionService component.
 * Provides core functionality and operations for MemberCollectionService.
 */
public interface MemberCollectionService {
        /**
     * Executes the operation.
     * @param productCollection the productCollection
     * @return the result of the operation
     */
    int add(MemberProductCollection productCollection);

        /**
     * Executes the operation.
     * @param productId the productId
     * @return the result of the operation
     */
    int delete(Long productId);

        /**
     * Executes the operation.
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    Page<MemberProductCollection> list(Integer pageNum, Integer pageSize);

        /**
     * Executes the operation.
     * @param productId the productId
     * @return the result of the operation
     */
    MemberProductCollection detail(Long productId);

        /**
     * Executes the operation.
     */
    void clear();
}
