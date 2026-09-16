package com.nexusengine.core.portal.service.impl;

import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.model.PmsReview;
import com.nexusengine.core.model.PmsReviewMedia;
import com.nexusengine.core.portal.domain.ReviewParam;
import com.nexusengine.core.portal.service.PmsPortalReviewService;
import com.nexusengine.core.portal.service.UmsMemberService;
import com.nexusengine.core.repository.PmsReviewMediaRepository;
import com.nexusengine.core.repository.PmsReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@lombok.RequiredArgsConstructor
public class PmsPortalReviewServiceImpl implements PmsPortalReviewService {
    private final PmsReviewRepository reviewRepository;
    private final PmsReviewMediaRepository reviewMediaRepository;
    private final UmsMemberService memberService;

    @Override
    public CommonPage<PmsReview> list(Long productId, Integer pageNum, Integer pageSize) {
        Page<PmsReview> page = reviewRepository.findByProductIdAndParentIdIsNullAndStatus(
                productId, 1, PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "id")));
        return CommonPage.restPage(page);
    }

    @Override
    public List<PmsReview> listReplies(Long parentId) {
        return reviewRepository.findByParentIdAndStatus(parentId, 1);
    }

    @Override
    public PmsReview create(ReviewParam param) {
        var member = memberService.getCurrentMember();
        PmsReview review = new PmsReview();
        review.setProductId(param.getProductId());
        review.setParentId(param.getParentId());
        review.setMemberId(member.getId());
        review.setRating(param.getRating());
        review.setContent(param.getContent());
        review.setLikeCount(0);
        review.setStatus(1); // Auto approve for now
        review.setCreatedTime(new Date());
        
        PmsReview saved = reviewRepository.save(review);
        
        if (param.getMediaList() != null && !param.getMediaList().isEmpty()) {
            List<PmsReviewMedia> mediaList = new ArrayList<>();
            for (var m : param.getMediaList()) {
                PmsReviewMedia media = new PmsReviewMedia();
                media.setReviewId(saved.getId());
                media.setMediaType(m.getMediaType());
                media.setMediaUrl(m.getMediaUrl());
                media.setSortOrder(m.getSortOrder());
                mediaList.add(media);
            }
            reviewMediaRepository.saveAll(mediaList);
            saved.setMediaList(mediaList);
        }
        return saved;
    }

    @Override
    public Map<String, String> uploadMedia(MultipartFile file) {
        // Simple local mock upload for testing
        try {
            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String dir = System.getProperty("user.dir") + "/uploads/";
            File dirFile = new File(dir);
            if (!dirFile.exists()) dirFile.mkdirs();
            File dest = new File(dir + filename);
            file.transferTo(dest);
            
            Map<String, String> result = new HashMap<>();
            // Assuming we serve uploads statically or mock it
            result.put("url", "/uploads/" + filename); 
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Upload failed", e);
        }
    }
}
