package com.nexusengine.core.admin.infra.dto;

import lombok.Builder;


import java.util.List;


public record BucketPolicyConfigDto(
    String Version,
    List<Statement> Statement
) {
    public record Statement(
        String Effect,
        String Principal,
        String Action,
        String Resource
    ) {
        @Builder(toBuilder=true)
        public Statement {}
    }

    @Builder(toBuilder=true)
    public BucketPolicyConfigDto {}
}
