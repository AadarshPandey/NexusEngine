package com.nexusengine.core.service;

import com.nexusengine.core.dto.*;
import com.nexusengine.core.model.OmsOrder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/10/11.
 */
public interface OmsOrderService {
    /**
     * Auto-generated documentation
     */
    List<OmsOrder> list(OmsOrderQueryParam queryParam, Integer pageSize, Integer pageNum);

    /**
     * Auto-generated documentation
     */
    @Transactional
    int delivery(List<OmsOrderDeliveryParam> deliveryParamList);

    /**
     * Auto-generated documentation
     */
    @Transactional
    int close(List<Long> ids, String note);

    /**
     * Auto-generated documentation
     */
    int delete(List<Long> ids);

    /**
     * Auto-generated documentation
     */
    OmsOrderDetail detail(Long id);

    /**
     * Auto-generated documentation
     */
    @Transactional
    int updateReceiverInfo(OmsReceiverInfoParam receiverInfoParam);

    /**
     * Auto-generated documentation
     */
    @Transactional
    int updateMoneyInfo(OmsMoneyInfoParam moneyInfoParam);

    /**
     * Auto-generated documentation
     */
    @Transactional
    int updateNote(Long id, String note, Integer status);
}
