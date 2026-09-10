package com.nexusengine.core.portal.domain;

import lombok.Getter;

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
