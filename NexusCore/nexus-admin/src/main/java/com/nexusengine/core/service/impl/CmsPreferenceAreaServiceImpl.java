package com.nexusengine.core.service.impl;

import com.nexusengine.core.repository.CmsPreferenceAreaRepository;
import com.nexusengine.core.model.CmsPreferenceArea;
import com.nexusengine.core.service.CmsPreferenceAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CmsPreferenceAreaServiceImpl implements CmsPreferenceAreaService {
    @Autowired
    private CmsPreferenceAreaRepository preferenceAreaRepository;

    @Override
    public List<CmsPreferenceArea> listAll() {
        return /* findAll() is acceptable for small reference tables */
        preferenceAreaRepository.findAll();
    }
}
