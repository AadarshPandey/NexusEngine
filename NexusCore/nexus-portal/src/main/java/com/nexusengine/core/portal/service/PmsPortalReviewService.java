package com.nexusengine.core.portal.service;

import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.model.PmsReview;
import com.nexusengine.core.portal.domain.ReviewParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

public interface PmsPortalReviewService {
    CommonPage<PmsReview> list(Long productId, Integer pageNum, Integer pageSize);
    List<PmsReview> listReplies(Long parentId);
    PmsReview create(ReviewParam param);
    Map<String, String> uploadMedia(MultipartFile file);
}
