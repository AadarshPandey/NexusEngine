package com.nexusengine.core.service.impl;

import com.nexusengine.core.repository.CmsPrefrenceAreaRepository;
import com.nexusengine.core.model.CmsPrefrenceArea;
import com.nexusengine.core.service.CmsPrefrenceAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CmsPrefrenceAreaServiceImpl implements CmsPrefrenceAreaService {
    @Autowired
    private CmsPrefrenceAreaRepository prefrenceAreaRepository;

    @Override
    public List<CmsPrefrenceArea> listAll() {
        return prefrenceAreaRepository.findAll();
    }
}
