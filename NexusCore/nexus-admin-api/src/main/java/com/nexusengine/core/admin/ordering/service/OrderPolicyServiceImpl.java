package com.nexusengine.core.admin.ordering.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.ordering.repository.OrderPolicyRepository;


import com.nexusengine.core.domain.ordering.entity.OrderPolicy;
import org.springframework.stereotype.Service;


/**
 * Represents the OrderPolicyServiceImpl component.
 * Provides core functionality and operations for OrderPolicyServiceImpl.
 */
@Service
@RequiredArgsConstructor
public class OrderPolicyServiceImpl implements OrderPolicyService {
    private final OrderPolicyRepository orderSettingRepository;

    @Override
    public OrderPolicy getItem(Long id) {
        return orderSettingRepository.findById(id).orElse(null);
    }

    @Override
    public int update(Long id, OrderPolicy orderSetting) {
        orderSetting.setId(id);
        orderSettingRepository.save(orderSetting);
        return 1;
    }
}
