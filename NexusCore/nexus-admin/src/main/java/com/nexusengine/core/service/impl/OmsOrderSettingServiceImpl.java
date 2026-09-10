package com.nexusengine.core.service.impl;

import com.nexusengine.core.repository.OmsOrderSettingRepository;
import com.nexusengine.core.model.OmsOrderSetting;
import com.nexusengine.core.service.OmsOrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Represents the OmsOrderSettingServiceImpl component.
 * Provides core functionality and operations for OmsOrderSettingServiceImpl.
 */
@Service
public class OmsOrderSettingServiceImpl implements OmsOrderSettingService {
    @Autowired
    private OmsOrderSettingRepository orderSettingRepository;

    @Override
    public OmsOrderSetting getItem(Long id) {
        return orderSettingRepository.findById(id).orElse(null);
    }

    @Override
    public int update(Long id, OmsOrderSetting orderSetting) {
        orderSetting.setId(id);
        orderSettingRepository.save(orderSetting);
        return 1;
    }
}
