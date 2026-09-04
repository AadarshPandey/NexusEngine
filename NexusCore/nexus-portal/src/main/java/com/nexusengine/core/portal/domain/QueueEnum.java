package com.nexusengine.core.portal.domain;

import lombok.Getter;

/**
 * Auto-generated documentation
 * Created by macro on 2018/9/14.
 */
@Getter
public enum QueueEnum {
    /**
     * Auto-generated documentation
     */
    QUEUE_ORDER_CANCEL("nexus.order.direct", "nexus.order.cancel", "nexus.order.cancel"),
    /**
     * Auto-generated documentation
     */
    QUEUE_TTL_ORDER_CANCEL("nexus.order.direct.ttl", "nexus.order.cancel.ttl", "nexus.order.cancel.ttl");

    /**
     * Auto-generated documentation
     */
    private final String exchange;
    /**
     * Auto-generated documentation
     */
    private final String name;
    /**
     * Auto-generated documentation
     */
    private final String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
