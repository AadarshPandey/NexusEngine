package com.nexusengine.core.service;

import com.nexusengine.core.model.SmsHomeRecommendSubject;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/11/7.
 */
public interface SmsHomeRecommendSubjectService {
    /**
     * Auto-generated documentation
     */
    @Transactional
    int create(List<SmsHomeRecommendSubject> recommendSubjectList);

    /**
     * Auto-generated documentation
     */
    int updateSort(Long id, Integer sort);

    /**
     * Auto-generated documentation
     */
    int delete(List<Long> ids);

    /**
     * Auto-generated documentation
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * Auto-generated documentation
     */
    List<SmsHomeRecommendSubject> list(String subjectName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
