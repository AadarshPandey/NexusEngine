package com.nexusengine.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.repository.CmsSubjectRepository;
import com.nexusengine.core.model.CmsSubject;
import com.nexusengine.core.service.CmsSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CmsSubjectServiceImpl implements CmsSubjectService {
    @Autowired
    private CmsSubjectRepository subjectRepository;

    @Override
    public List<CmsSubject> listAll() {
        return subjectRepository.findAll();
    }

    @Override
    public List<CmsSubject> list(String keyword, Integer pageSize, Integer pageNum) {
        if (StrUtil.isNotEmpty(keyword)) {
            return subjectRepository.findByTitleContaining(keyword, PageRequest.of(pageNum, pageSize));
        }
        return subjectRepository.findAll(PageRequest.of(pageNum, pageSize)).getContent();
    }
}
