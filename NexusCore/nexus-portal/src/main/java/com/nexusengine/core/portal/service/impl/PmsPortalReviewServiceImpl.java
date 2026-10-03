package com.nexusengine.core.portal.service.impl;

import com.nexusengine.core.model.PmsCommentReply;
import com.nexusengine.core.repository.PmsCommentReplyRepository;
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
    private final PmsCommentReplyRepository commentReplyRepository;
    private final com.nexusengine.core.common.service.RedisService redisService;

    @Override
    public CommonPage<PmsReview> list(Long productId, Integer pageNum, Integer pageSize) {
        Page<PmsReview> page = reviewRepository.findByProductIdAndParentIdIsNullAndStatus(
                productId, 1, PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "id")));
        return CommonPage.restPage(page);
    }

    @Override
    public List<PmsReview> listReplies(Long parentId) {
        List<PmsReview> allDescendants = new ArrayList<>();
        collectDescendants(parentId, allDescendants);
        return allDescendants;
    }

    private void collectDescendants(Long parentId, List<PmsReview> allDescendants) {
        List<PmsReview> children = reviewRepository.findByParentIdAndStatus(parentId, 1);
        if (children != null && !children.isEmpty()) {
            allDescendants.addAll(children);
            for (PmsReview child : children) {
                collectDescendants(child.getId(), allDescendants);
            }
        }
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
            
            io.minio.MinioClient minioClient = io.minio.MinioClient.builder()
                    .endpoint("http://minio:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();
                    
            for (var m : param.mediaList()) {
                String mediaUrl = m.mediaUrl();
                // Move from temp folder to final folder if it's a new upload
                if (mediaUrl.contains("public/users/comments/temp_" + member.getId())) {
                    try {
                        String oldObjectName = mediaUrl.substring(mediaUrl.indexOf("public/users/comments/temp_"));
                        String uuidAndExt = oldObjectName.substring(oldObjectName.lastIndexOf("/") + 1);
                        String newObjectName = "public/users/comments/" + member.getId() + "/" + param.productId() + "/" + saved.getId() + "_" + uuidAndExt;
                        
                        // Just copy for now, temp folder can be cleaned by a cron job or we can delete it directly
                        minioClient.copyObject(
                                io.minio.CopyObjectArgs.builder()
                                        .bucket("nexus-media")
                                        .object(newObjectName)
                                        .source(io.minio.CopySource.builder()
                                                .bucket("nexus-media")
                                                .object(oldObjectName)
                                                .build())
                                        .build());
                        
                        mediaUrl = "http://localhost:9000/nexus-media/" + newObjectName;
                    } catch (Exception e) {
                        org.slf4j.LoggerFactory.getLogger(PmsPortalReviewServiceImpl.class).error("Failed to copy media object", e);
                    }
                }
                
                PmsReviewMedia media = new PmsReviewMedia();
                media.setReviewId(saved.getId());
                media.setMediaType(m.mediaType());
                media.setMediaUrl(mediaUrl);
                media.setSortOrder(m.sortOrder());
                mediaList.add(media);
                picUrls.add(mediaUrl);
            }
            reviewMediaRepository.saveAll(mediaList);
            saved.setMediaList(mediaList);
            pics = String.join(",", picUrls);
        }
        
        // Sync to PmsComment
        if (param.parentId() == null) {
            // It's a root comment
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
            }
            commentRepository.save(comment);
        } else {
            // It's a reply! Save the child comment in PmsComment first
            PmsReview parentReview = reviewRepository.findById(param.parentId()).orElse(null);
            if (parentReview != null) {
                PmsComment parentComment = commentRepository.findFirstByProductIdAndContentOrderByIdDesc(parentReview.getProductId(), parentReview.getContent());
                if (parentComment != null) {
                    // 1. Create the new child comment
                    PmsComment childComment = new PmsComment();
                    childComment.setProductId(parentReview.getProductId());
                    childComment.setMemberNickName(member.getNickname());
                    childComment.setMemberIcon(member.getIcon());
                    childComment.setMemberIp("127.0.0.1");
                    childComment.setContent(param.content());
                    childComment.setCreateTime(java.time.LocalDateTime.now());
                    childComment.setShowStatus(1);
                    childComment.setStar(param.rating() == null ? 5 : param.rating());
                    childComment.setReadCount(0);
                    childComment.setLikesCount(0);
                    childComment.setReplyCount(0);
                    PmsProduct product = productRepository.findById(param.productId()).orElse(null);
                    if (product != null) {
                        childComment.setProductName(product.getName());
                        childComment.setProductAttribute(product.getSubTitle());
                    }
                    childComment = commentRepository.save(childComment);

                    // 2. Create the relationship link
                    PmsCommentReply reply = new PmsCommentReply();
                    reply.setCommentId(parentComment.getId()); 
                    reply.setReplyCommentId(childComment.getId());
                    commentReplyRepository.save(reply);
                    
                    // Increment reply_count on the parent comment
                    parentComment.setReplyCount((parentComment.getReplyCount() == null ? 0 : parentComment.getReplyCount()) + 1);
                    commentRepository.save(parentComment);
                }
            }
        }
        
        // Universally update brand's comment count for both comments and replies
        PmsProduct product = productRepository.findById(param.productId()).orElse(null);
        if (product != null && product.getBrandId() != null) {
            PmsBrand brand = brandRepository.findById(product.getBrandId()).orElse(null);
            if (brand != null) {
                brand.setProductCommentCount((brand.getProductCommentCount() == null ? 0 : brand.getProductCommentCount()) + 1);
                brandRepository.save(brand);
            }
        }
        
        return saved;
    }

    @Override
    public Map<String, String> uploadMedia(MultipartFile file) {
        try {
            var member = memberService.getCurrentMember();
            Long userId = member != null ? member.getId() : 0L;
            String originalFilename = file.getOriginalFilename();
            String ext = originalFilename != null && originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".png";
            String uuid = UUID.randomUUID().toString();
            String objectName = "public/users/comments/temp_" + userId + "/" + uuid + ext;
            
            io.minio.MinioClient minioClient = io.minio.MinioClient.builder()
                    .endpoint("http://minio:9000") // Docker internal host
                    .credentials("minioadmin", "minioadmin")
                    .build();
                    
            minioClient.putObject(io.minio.PutObjectArgs.builder()
                    .bucket("nexus-media")
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            
            Map<String, String> result = new HashMap<>();
            result.put("url", "http://localhost:9000/nexus-media/" + objectName); 
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Upload failed", e);
        }
    }

    @Override
    public Integer like(Long id) {
        var member = memberService.getCurrentMember();
        if (member == null) {
            throw new RuntimeException("Must be logged in to like");
        }
        
        String redisKey = "review:like:" + id;
        Long userId = member.getId();
        
        // Check if already liked
        if (redisService.sIsMember(redisKey, userId)) {
            // Already liked, just return current count or maybe we can implement unlike here.
            // For now, just return current count.
            PmsReview review = reviewRepository.findById(id).orElse(null);
            return review != null && review.getLikeCount() != null ? review.getLikeCount() : 0;
        }

        // Add to set
        redisService.sAdd(redisKey, (Object) userId);

        PmsReview review = reviewRepository.findById(id).orElse(null);
        if (review != null) {
            Integer currentLikes = review.getLikeCount() == null ? 0 : review.getLikeCount();
            review.setLikeCount(currentLikes + 1);
            reviewRepository.save(review);
            
            // Also update PmsComment if needed
            PmsComment comment = commentRepository.findFirstByProductIdAndContentOrderByIdDesc(review.getProductId(), review.getContent());
            if (comment != null) {
                Integer commentLikes = comment.getLikesCount() == null ? 0 : comment.getLikesCount();
                comment.setLikesCount(commentLikes + 1);
                commentRepository.save(comment);
            }
            return review.getLikeCount();
        }
        return 0;
    }
}
