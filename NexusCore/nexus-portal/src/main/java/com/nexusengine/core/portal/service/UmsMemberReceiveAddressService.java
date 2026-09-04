package com.nexusengine.core.portal.service;

import com.nexusengine.core.model.UmsMemberReceiveAddress;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/8/28.
 */
public interface UmsMemberReceiveAddressService {
    /**
     * Auto-generated documentation
     */
    int add(UmsMemberReceiveAddress address);

    /**
     * Auto-generated documentation
     * Auto-generated documentation
     */
    int delete(Long id);

    /**
     * Auto-generated documentation
     * Auto-generated documentation
     * Auto-generated documentation
     */
    @Transactional
    int update(Long id, UmsMemberReceiveAddress address);

    /**
     * Auto-generated documentation
     */
    List<UmsMemberReceiveAddress> list();

    /**
     * Auto-generated documentation
     * Auto-generated documentation
     */
    UmsMemberReceiveAddress getItem(Long id);
}
