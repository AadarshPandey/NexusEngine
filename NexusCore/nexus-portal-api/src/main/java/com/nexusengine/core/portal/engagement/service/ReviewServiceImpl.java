package com.nexusengine.core.portal.engagement.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.common.api.CommonPage;

import com.nexusengine.core.domain.engagement.entity.ProductReview;
import com.nexusengine.core.domain.engagement.entity.ReviewMedia;
import com.nexusengine.core.domain.engagement.repository.ReviewMediaRepository;
import com.nexusengine.core.domain.engagement.repository.ProductReviewRepository;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import com.nexusengine.core.domain.catalog.repository.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import com.nexusengine.core.portal.customer.service.MemberService;
import com.nexusengine.core.portal.engagement.dto.ReviewParam;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ProductReviewRepository reviewRepository;
    private final ReviewMediaRepository reviewMediaRepository;
    private final MemberService memberService;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final com.nexusengine.core.common.service.RedisService redisService;

    @Override
    public CommonPage<ProductReview> list(Long productId, Integer pageNum, Integer pageSize) {
        Page<ProductReview> page = reviewRepository.findByProductIdAndParentIdIsNullAndStatus(
                productId, 1, PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "id")));
        enrichReviewStates(page.getContent());
        return CommonPage.restPage(page);
    }

    @Override
    public List<ProductReview> listReplies(Long parentId) {
        List<ProductReview> allDescendants = new ArrayList<>();
        collectDescendants(parentId, allDescendants);
        enrichReviewStates(allDescendants);
        return allDescendants;
    }

    private void enrichReviewStates(List<ProductReview> reviews) {
        var member = memberService.getCurrentMember();
        Long userId = member != null ? member.getId() : null;
        if (userId == null) {
            reviews.forEach(r -> { r.setUserLiked(false); r.setUserDisliked(false); });
            return;
        }
        for (ProductReview r : reviews) {
            r.setUserLiked(redisService.sIsMember("review:like:" + r.getId(), userId));
            r.setUserDisliked(redisService.sIsMember("review:dislike:" + r.getId(), userId));
        }
    }

    private void collectDescendants(Long parentId, List<ProductReview> allDescendants) {
        List<ProductReview> children = reviewRepository.findByParentIdAndStatus(parentId, 1);
        if (children != null && !children.isEmpty()) {
            allDescendants.addAll(children);
            for (ProductReview child : children) {
                collectDescendants(child.getId(), allDescendants);
            }
        }
    }

    @Override
    public ProductReview create(ReviewParam param) {
        var member = memberService.getCurrentMember();
        ProductReview review = new ProductReview();
        review.setProductId(param.productId());
        review.setParentId(param.parentId());
        review.setMemberId(member.getId());
        review.setRating(param.rating());
        review.setContent(param.content());
        review.setLikeCount(0);
        review.setDislikeCount(0);
        review.setStatus(1); // Auto approve for now
        review.setCreatedTime(java.time.OffsetDateTime.now());

        ProductReview saved = reviewRepository.save(review);

        String pics = "";
        if (param.attachmentList() != null && !param.attachmentList().isEmpty()) {
            List<ReviewMedia> attachmentList = new ArrayList<>();
            List<String> picUrls = new ArrayList<>();

            io.minio.MinioClient minioClient = io.minio.MinioClient.builder()
                    .endpoint("http://minio:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

            for (var m : param.attachmentList()) {
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
                        org.slf4j.LoggerFactory.getLogger(ReviewServiceImpl.class).error("Failed to copy media object", e);
                    }
                }

                ReviewMedia media = new ReviewMedia();
                media.setReviewId(saved.getId());
                media.setMediaType(m.mediaType());
                media.setMediaUrl(mediaUrl);
                media.setSortOrder(m.sortOrder());
                attachmentList.add(media);
                picUrls.add(mediaUrl);
            }
            reviewMediaRepository.saveAll(attachmentList);
            saved.setAttachmentList(attachmentList);
            pics = String.join(",", picUrls);
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

        String likeKey = "review:like:" + id;
        String dislikeKey = "review:dislike:" + id;
        Long userId = member.getId();

        ProductReview review = reviewRepository.findById(id).orElse(null);
        if (review == null) return 0;

        if (redisService.sIsMember(likeKey, userId)) {
            // Undo like
            redisService.sRemove(likeKey, (Object) userId);
            review.setLikeCount(Math.max(0, (review.getLikeCount() == null ? 0 : review.getLikeCount()) - 1));
        } else {
            // Do like
            redisService.sAdd(likeKey, (Object) userId);
            review.setLikeCount((review.getLikeCount() == null ? 0 : review.getLikeCount()) + 1);

            // If already disliked, undo dislike
            if (redisService.sIsMember(dislikeKey, userId)) {
                redisService.sRemove(dislikeKey, (Object) userId);
                review.setDislikeCount(Math.max(0, (review.getDislikeCount() == null ? 0 : review.getDislikeCount()) - 1));
            }
        }

        reviewRepository.save(review);

        return review.getLikeCount();
    }

    @Override
    public Integer dislike(Long id) {
        var member = memberService.getCurrentMember();
        if (member == null) {
            throw new RuntimeException("Must be logged in to dislike");
        }

        String likeKey = "review:like:" + id;
        String dislikeKey = "review:dislike:" + id;
        Long userId = member.getId();

        ProductReview review = reviewRepository.findById(id).orElse(null);
        if (review == null) return 0;

        if (redisService.sIsMember(dislikeKey, userId)) {
            // Undo dislike
            redisService.sRemove(dislikeKey, (Object) userId);
            review.setDislikeCount(Math.max(0, (review.getDislikeCount() == null ? 0 : review.getDislikeCount()) - 1));
        } else {
            // Do dislike
            redisService.sAdd(dislikeKey, (Object) userId);
            review.setDislikeCount((review.getDislikeCount() == null ? 0 : review.getDislikeCount()) + 1);

            // If already liked, undo like
            if (redisService.sIsMember(likeKey, userId)) {
                redisService.sRemove(likeKey, (Object) userId);
                review.setLikeCount(Math.max(0, (review.getLikeCount() == null ? 0 : review.getLikeCount()) - 1));
            }
        }

        reviewRepository.save(review);

        return review.getDislikeCount();
    }
}
