package com.nexusengine.core.portal.engagement.dto;

import java.util.List;



public record ReviewParam(
    Long productId,
    Long parentId,
    Integer rating,
    String content,
    List<ReviewMediaParam> attachmentList,
    Long orderId
) {
    public ReviewParam(Long productId, Long parentId, Integer rating, String content, List<ReviewMediaParam> attachmentList) {
        this(productId, parentId, rating, content, attachmentList, null);
    }
}
