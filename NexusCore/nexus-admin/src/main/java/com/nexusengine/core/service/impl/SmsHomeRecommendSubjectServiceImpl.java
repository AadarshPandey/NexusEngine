package com.nexusengine.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.repository.SmsHomeRecommendSubjectRepository;
import com.nexusengine.core.model.SmsHomeRecommendSubject;
import com.nexusengine.core.service.SmsHomeRecommendSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsHomeRecommendSubjectServiceImpl implements SmsHomeRecommendSubjectService {
    @Autowired
    private SmsHomeRecommendSubjectRepository smsHomeRecommendSubjectRepository;
    
    @Override
    public int create(List<SmsHomeRecommendSubject> recommendSubjectList) {
        for (SmsHomeRecommendSubject recommendSubject : recommendSubjectList) {
            recommendSubject.setRecommendStatus(1);
            recommendSubject.setSort(0);
            smsHomeRecommendSubjectRepository.save(recommendSubject);
        }
        return recommendSubjectList.size();
    }

    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeRecommendSubject recommendSubject = smsHomeRecommendSubjectRepository.findById(id).orElse(new SmsHomeRecommendSubject());
        recommendSubject.setId(id);
        recommendSubject.setSort(sort);
        smsHomeRecommendSubjectRepository.save(recommendSubject);
        return 1;
    }

    @Override
    public int delete(List<Long> ids) {
        smsHomeRecommendSubjectRepository.deleteAllById(ids);
        return ids.size();
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        List<SmsHomeRecommendSubject> list = smsHomeRecommendSubjectRepository.findAllById(ids);
        for(SmsHomeRecommendSubject record : list){
            record.setRecommendStatus(recommendStatus);
            smsHomeRecommendSubjectRepository.save(record);
        }
        return list.size();
    }

    @Override
    public List<SmsHomeRecommendSubject> list(String subjectName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        return smsHomeRecommendSubjectRepository.findAll(PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "sort"))).getContent();
    }
}
