package com.nexusengine.core.portal.engagement.service;

import com.nexusengine.core.domain.engagement.entity.ProductReview;
import com.nexusengine.core.portal.engagement.dto.MediaUploadResponse;
import com.nexusengine.core.portal.engagement.dto.ReviewParam;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ReviewService {
    Page<ProductReview> list(Long productId, Integer pageNum, Integer pageSize);

    List<ProductReview> listReplies(Long parentId);

    ProductReview create(ReviewParam param);

    MediaUploadResponse uploadMedia(MultipartFile file);

    Integer like(Long id);

    Integer dislike(Long id);
}
