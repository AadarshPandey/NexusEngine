package com.nexusengine.core.admin.catalog.dto;

public record CategoryResponse(
        Long id,
        Long parentId,
        String name,
        Integer depth,
        Integer productCount,
        Boolean isInNavigation,
        Boolean isVisible,
        String iconUrl) {}
