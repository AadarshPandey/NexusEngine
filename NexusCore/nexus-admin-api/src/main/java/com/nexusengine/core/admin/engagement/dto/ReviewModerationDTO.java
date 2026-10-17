package com.nexusengine.core.admin.engagement.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record ReviewModerationDTO(
    Long id,
    Long productId,
    String productName,
    String productPic,
    String brandName,
    Long memberId,
    String memberUsername,
    String memberNickName,
    Integer rating,
    String content,
    Integer status, // 0 = Pending, 1 = Approved, 2 = Rejected
    Integer likeCount,
    Integer dislikeCount,
    List<String> mediaUrls,
    OffsetDateTime createdAt
) {}
