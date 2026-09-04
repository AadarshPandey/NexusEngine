package com.nexusengine.core.service;

import com.nexusengine.core.model.CmsSubject;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/6/1.
 */
public interface CmsSubjectService {
    /**
     * Auto-generated documentation
     */
    List<CmsSubject> listAll();

    /**
     * Auto-generated documentation
     */
    List<CmsSubject> list(String keyword, Integer pageNum, Integer pageSize);
}
