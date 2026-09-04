package com.nexusengine.core.portal.service;

import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.portal.domain.ConfirmOrderResult;
import com.nexusengine.core.portal.domain.OmsOrderDetail;
import com.nexusengine.core.portal.domain.OrderParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Auto-generated documentation
 * Created by macro on 2018/8/30.
 */
public interface OmsPortalOrderService {
    /**
     * Auto-generated documentation
     */
    ConfirmOrderResult generateConfirmOrder(List<Long> cartIds);

    /**
     * Auto-generated documentation
     */
    @Transactional
    Map<String, Object> generateOrder(OrderParam orderParam);

    /**
     * Auto-generated documentation
     */
    @Transactional
    Integer paySuccess(Long orderId, Integer payType);

    /**
     * Auto-generated documentation
     */
    @Transactional
    Integer cancelTimeOutOrder();

    /**
     * Auto-generated documentation
     */
    @Transactional
    void cancelOrder(Long orderId);

    /**
     * Auto-generated documentation
     */
    void sendDelayMessageCancelOrder(Long orderId);

    /**
     * Auto-generated documentation
     */
    void confirmReceiveOrder(Long orderId);

    /**
     * Auto-generated documentation
     */
    CommonPage<OmsOrderDetail> list(Integer status, Integer pageNum, Integer pageSize);

    /**
     * Auto-generated documentation
     */
    OmsOrderDetail detail(Long orderId);

    /**
     * Auto-generated documentation
     */
    void deleteOrder(Long orderId);

    /**
     * Auto-generated documentation
     */
    @Transactional
    void paySuccessByOrderSn(String orderSn, Integer payType);
}
