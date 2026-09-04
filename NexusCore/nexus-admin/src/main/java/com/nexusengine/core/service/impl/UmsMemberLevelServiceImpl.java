package com.nexusengine.core.service.impl;

import com.nexusengine.core.repository.UmsMemberLevelRepository;
import com.nexusengine.core.model.UmsMemberLevel;
import com.nexusengine.core.service.UmsMemberLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UmsMemberLevelServiceImpl implements UmsMemberLevelService {
    @Autowired
    private UmsMemberLevelRepository memberLevelRepository;
    
    @Override
    public List<UmsMemberLevel> list(Integer defaultStatus) {
        if (defaultStatus != null) {
            return memberLevelRepository.findByDefaultStatus(defaultStatus);
        }
        return memberLevelRepository.findAll();
    }
}
