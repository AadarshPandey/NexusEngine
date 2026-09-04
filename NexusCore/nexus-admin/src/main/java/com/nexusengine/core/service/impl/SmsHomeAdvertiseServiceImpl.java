package com.nexusengine.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.nexusengine.core.repository.SmsHomeAdvertiseRepository;
import com.nexusengine.core.model.SmsHomeAdvertise;
import com.nexusengine.core.service.SmsHomeAdvertiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsHomeAdvertiseServiceImpl implements SmsHomeAdvertiseService {
    @Autowired
    private SmsHomeAdvertiseRepository advertiseRepository;

    @Override
    public int create(SmsHomeAdvertise advertise) {
        advertise.setClickCount(0);
        advertise.setOrderCount(0);
        advertiseRepository.save(advertise);
        return 1;
    }

    @Override
    public int delete(List<Long> ids) {
        advertiseRepository.deleteAllById(ids);
        return ids.size();
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        SmsHomeAdvertise record = advertiseRepository.findById(id).orElse(new SmsHomeAdvertise());
        record.setId(id);
        record.setStatus(status);
        advertiseRepository.save(record);
        return 1;
    }

    @Override
    public SmsHomeAdvertise getItem(Long id) {
        return advertiseRepository.findById(id).orElse(null);
    }

    @Override
    public int update(Long id, SmsHomeAdvertise advertise) {
        advertise.setId(id);
        advertiseRepository.save(advertise);
        return 1;
    }

    @Override
    public List<SmsHomeAdvertise> list(String name, Integer type, String endTime, Integer pageSize, Integer pageNum) {
        return advertiseRepository.findAll(PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "sort"))).getContent();
    }
}
