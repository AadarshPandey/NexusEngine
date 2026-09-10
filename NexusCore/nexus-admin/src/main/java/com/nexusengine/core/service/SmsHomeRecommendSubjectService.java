package com.nexusengine.core.service;

import com.nexusengine.core.model.SmsHomeRecommendSubject;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Represents the SmsHomeRecommendSubjectService component.
 * Provides core functionality and operations for SmsHomeRecommendSubjectService.
 */
public interface SmsHomeRecommendSubjectService {
        /**
     * Executes the operation.
     * @param recommendSubjectList the recommendSubjectList
     * @return the result of the operation
     */
    @Transactional
    int create(List<SmsHomeRecommendSubject> recommendSubjectList);

        /**
     * Executes the operation.
     * @param id the id
     * @param sort the sort
     * @return the result of the operation
     */
    int updateSort(Long id, Integer sort);

        /**
     * Executes the operation.
     * @param ids the ids
     * @return the result of the operation
     */
    int delete(List<Long> ids);

        /**
     * Executes the operation.
     * @param ids the ids
     * @param recommendStatus the recommendStatus
     * @return the result of the operation
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

        /**
     * Executes the operation.
     * @param subjectName the subjectName
     * @param recommendStatus the recommendStatus
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    List<SmsHomeRecommendSubject> list(String subjectName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
