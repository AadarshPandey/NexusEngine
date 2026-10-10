package com.nexusengine.core.portal.engagement.dto;
public record ReviewMediaParam(
    String mediaType,
    String mediaUrl,
    Integer sortOrder
) {}