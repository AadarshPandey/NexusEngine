package com.nexusengine.core.portal.service;

import com.nexusengine.core.portal.domain.MemberReadHistory;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Represents the MemberReadHistoryService component.
 * Provides core functionality and operations for MemberReadHistoryService.
 */
public interface MemberReadHistoryService {
        /**
     * Executes the operation.
     * @param memberReadHistory the memberReadHistory
     * @return the result of the operation
     */
    int create(MemberReadHistory memberReadHistory);

        /**
     * Executes the operation.
     * @param ids the ids
     * @return the result of the operation
     */
    int delete(List<String> ids);

        /**
     * Executes the operation.
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    Page<MemberReadHistory> list(Integer pageNum, Integer pageSize);

        /**
     * Executes the operation.
     */
    void clear();
}
