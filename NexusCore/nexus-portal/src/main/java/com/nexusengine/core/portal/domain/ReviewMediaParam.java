package com.nexusengine.core.portal.domain;
public record ReviewMediaParam(
    String mediaType,
    String mediaUrl,
    Integer sortOrder
) {}