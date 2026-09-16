package com.nexusengine.core.service.impl;

import com.nexusengine.core.repository.UmsMemberLevelRepository;
import com.nexusengine.core.model.UmsMemberLevel;
import com.nexusengine.core.service.UmsMemberLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@lombok.RequiredArgsConstructor
public class UmsMemberLevelServiceImpl implements UmsMemberLevelService {
    private final UmsMemberLevelRepository memberLevelRepository;
    
    @Override
    public List<UmsMemberLevel> list(Integer defaultStatus) {
        if (defaultStatus != null) {
            return memberLevelRepository.findByDefaultStatus(defaultStatus);
        }
        return /* findAll() is acceptable for small reference tables */
        memberLevelRepository.findAll();
    }
}
