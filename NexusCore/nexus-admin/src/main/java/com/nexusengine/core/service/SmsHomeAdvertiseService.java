package com.nexusengine.core.service;

import com.nexusengine.core.model.SmsHomeAdvertise;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/11/7.
 */
public interface SmsHomeAdvertiseService {
    /**
     * Auto-generated documentation
     */
    int create(SmsHomeAdvertise advertise);

    /**
     * Auto-generated documentation
     */
    int delete(List<Long> ids);

    /**
     * Auto-generated documentation
     */
    int updateStatus(Long id, Integer status);

    /**
     * Auto-generated documentation
     */
    SmsHomeAdvertise getItem(Long id);

    /**
     * Auto-generated documentation
     */
    int update(Long id, SmsHomeAdvertise advertise);

    /**
     * Auto-generated documentation
     */
    List<SmsHomeAdvertise> list(String name, Integer type, String endTime, Integer pageSize, Integer pageNum);
}
