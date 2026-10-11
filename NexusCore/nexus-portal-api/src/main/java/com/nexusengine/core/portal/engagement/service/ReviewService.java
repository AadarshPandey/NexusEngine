package com.nexusengine.core.portal.engagement.service;

import com.nexusengine.core.common.api.CommonPage;

import com.nexusengine.core.domain.engagement.entity.ProductReview;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;
import com.nexusengine.core.portal.engagement.dto.ReviewParam;


public interface ReviewService {
    CommonPage<ProductReview> list(Long productId, Integer pageNum, Integer pageSize);
    List<ProductReview> listReplies(Long parentId);
    ProductReview create(ReviewParam param);
    Map<String, String> uploadMedia(MultipartFile file);
    Integer like(Long id);
    Integer dislike(Long id);
}
