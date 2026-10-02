package com.nexusengine.core.portal.service.impl;

import com.nexusengine.core.common.api.CommonPage;
import com.nexusengine.core.model.PmsReview;
import com.nexusengine.core.model.PmsReviewMedia;
import com.nexusengine.core.model.PmsComment;
import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.model.PmsBrand;
import com.nexusengine.core.portal.domain.ReviewParam;
import com.nexusengine.core.portal.service.PmsPortalReviewService;
import com.nexusengine.core.portal.service.UmsMemberService;
import com.nexusengine.core.repository.PmsReviewMediaRepository;
import com.nexusengine.core.repository.PmsReviewRepository;
import com.nexusengine.core.repository.PmsCommentRepository;
import com.nexusengine.core.repository.PmsProductRepository;
import com.nexusengine.core.repository.PmsBrandRepository;
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
    private final PmsCommentRepository commentRepository;
    private final PmsProductRepository productRepository;
    private final PmsBrandRepository brandRepository;

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
        review.setProductId(param.productId());
        review.setParentId(param.parentId());
        review.setMemberId(member.getId());
        review.setRating(param.rating());
        review.setContent(param.content());
        review.setLikeCount(0);
        review.setStatus(1); // Auto approve for now
        review.setCreatedTime(java.time.LocalDateTime.now());
        
        PmsReview saved = reviewRepository.save(review);
        
        String pics = "";
        if (param.mediaList() != null && !param.mediaList().isEmpty()) {
            List<PmsReviewMedia> mediaList = new ArrayList<>();
            List<String> picUrls = new ArrayList<>();
            for (var m : param.mediaList()) {
                PmsReviewMedia media = new PmsReviewMedia();
                media.setReviewId(saved.getId());
                media.setMediaType(m.mediaType());
                media.setMediaUrl(m.mediaUrl());
                media.setSortOrder(m.sortOrder());
                mediaList.add(media);
                picUrls.add(m.mediaUrl());
            }
            reviewMediaRepository.saveAll(mediaList);
            saved.setMediaList(mediaList);
            pics = String.join(",", picUrls);
        }
        
        // Also save to PmsComment for backward compatibility
        PmsComment comment = new PmsComment();
        comment.setProductId(param.productId());
        comment.setMemberNickName(member.getNickname());
        comment.setMemberIcon(member.getIcon());
        comment.setStar(param.rating());
        comment.setContent(param.content());
        comment.setCreateTime(java.time.LocalDateTime.now());
        comment.setShowStatus(1);
        comment.setLikesCount(0);
        comment.setReadCount(0);
        comment.setReplyCount(0);
        comment.setPics(pics);
        
        PmsProduct product = productRepository.findById(param.productId()).orElse(null);
        if (product != null) {
            comment.setProductName(product.getName());
            comment.setProductAttribute(product.getSubTitle());
            
            if (product.getBrandId() != null) {
                PmsBrand brand = brandRepository.findById(product.getBrandId()).orElse(null);
                if (brand != null) {
                    brand.setProductCommentCount((brand.getProductCommentCount() == null ? 0 : brand.getProductCommentCount()) + 1);
                    brandRepository.save(brand);
                }
            }
        }
        commentRepository.save(comment);
        
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
