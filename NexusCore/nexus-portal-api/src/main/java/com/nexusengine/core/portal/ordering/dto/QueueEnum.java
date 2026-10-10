package com.nexusengine.core.portal.ordering.dto;

import lombok.Getter;
import com.nexusengine.core.portal.catalog.dto.*;
import com.nexusengine.core.portal.catalog.service.*;
import com.nexusengine.core.portal.catalog.dao.*;
import com.nexusengine.core.portal.payment.service.*;
import com.nexusengine.core.portal.ordering.dto.*;
import com.nexusengine.core.portal.ordering.service.*;
import com.nexusengine.core.portal.ordering.dao.*;
import com.nexusengine.core.portal.customer.dto.*;
import com.nexusengine.core.portal.customer.service.*;
import com.nexusengine.core.portal.promotion.dto.*;
import com.nexusengine.core.portal.promotion.service.*;
import com.nexusengine.core.portal.promotion.strategy.*;
import com.nexusengine.core.portal.ai.service.*;
import com.nexusengine.core.portal.home.dto.*;
import com.nexusengine.core.portal.home.service.*;
import com.nexusengine.core.portal.engagement.dto.*;
import com.nexusengine.core.portal.engagement.service.*;

import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;


    /**
     * The { property.
     */
@Getter
public enum QueueEnum {
        /**
     * Executes the operation.
     * @return the result of the operation
     */
    QUEUE_ORDER_CANCEL("nexus.order.direct", "nexus.order.cancel", "nexus.order.cancel"),
        /**
     * Executes the operation.
     * @return the result of the operation
     */
    QUEUE_TTL_ORDER_CANCEL("nexus.order.direct.ttl", "nexus.order.cancel.ttl", "nexus.order.cancel.ttl"),
    /**
     * Flash Sale Order Queue
     */
    QUEUE_FLASH_ORDER("nexus.flash.direct", "nexus.flash.order", "nexus.flash.order");

        /**
     * The exchange property.
     */
    private final String exchange;
        /**
     * The name property.
     */
    private final String name;
        /**
     * The routeKey property.
     */
    private final String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
