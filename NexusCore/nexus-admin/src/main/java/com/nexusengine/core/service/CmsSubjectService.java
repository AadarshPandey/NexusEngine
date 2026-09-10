package com.nexusengine.core.service;

import com.nexusengine.core.model.CmsSubject;

import java.util.List;

/**
 * Represents the CmsSubjectService component.
 * Provides core functionality and operations for CmsSubjectService.
 */
public interface CmsSubjectService {
        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<CmsSubject> listAll();

        /**
     * Executes the operation.
     * @param keyword the keyword
     * @param pageNum the pageNum
     * @param pageSize the pageSize
     * @return the result of the operation
     */
    List<CmsSubject> list(String keyword, Integer pageNum, Integer pageSize);
}
