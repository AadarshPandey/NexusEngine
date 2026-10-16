package com.nexusengine.core.portal.engagement.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.engagement.entity.ProductReview;
import com.nexusengine.core.domain.engagement.entity.ReviewMedia;
import com.nexusengine.core.domain.engagement.repository.ReviewMediaRepository;
import com.nexusengine.core.domain.engagement.repository.ProductReviewRepository;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import com.nexusengine.core.domain.catalog.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.util.*;
import com.nexusengine.core.portal.customer.service.MemberService;
import com.nexusengine.core.portal.engagement.dto.MediaUploadResponse;
import com.nexusengine.core.portal.engagement.dto.ReviewParam;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ProductReviewRepository reviewRepository;
    private final ReviewMediaRepository reviewMediaRepository;
    private final MemberService memberService;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final com.nexusengine.core.domain.ordering.repository.CustomerOrderRepository customerOrderRepository;
    private final org.springframework.data.redis.core.StringRedisTemplate stringRedisTemplate;
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket-name:nexus-media}")
    private String bucketName;
    @Value("${spring.cloud.aws.s3.endpoint:http://localhost:4566}")
    private String s3Endpoint;

    @Override
    public Page<ProductReview> list(Long productId, Integer pageNum, Integer pageSize) {
        Page<ProductReview> page = reviewRepository.findByProductIdAndParentIdIsNullAndStatus(
                productId, 1, PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "id")));
        enrichReviewStates(page.getContent());
        return page;
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
            String uidStr = String.valueOf(userId);
            r.setUserLiked(Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember("review:like:" + r.getId(), uidStr)));
            r.setUserDisliked(Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember("review:dislike:" + r.getId(), uidStr)));
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

        if (param.attachmentList() != null && !param.attachmentList().isEmpty()) {
            List<ReviewMedia> attachmentList = new ArrayList<>();

            for (var m : param.attachmentList()) {
                String mediaUrl = m.mediaUrl();
                if (mediaUrl.contains("public/users/comments/temp_" + member.getId())) {
                    try {
                        String oldObjectName = mediaUrl.substring(mediaUrl.indexOf("public/users/comments/temp_"));
                        String uuidAndExt = oldObjectName.substring(oldObjectName.lastIndexOf("/") + 1);
                        String newObjectName = "public/users/comments/" + member.getId() + "/" + param.productId() + "/" + saved.getId() + "_" + uuidAndExt;

                        s3Client.copyObject(
                                CopyObjectRequest.builder()
                                        .sourceBucket(bucketName)
                                        .sourceKey(oldObjectName)
                                        .destinationBucket(bucketName)
                                        .destinationKey(newObjectName)
                                        .build());

                        mediaUrl = s3Endpoint + "/" + bucketName + "/" + newObjectName;
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
            }
            reviewMediaRepository.saveAll(attachmentList);
            saved.setAttachmentList(attachmentList);
        }

        if (param.orderId() != null) {
            customerOrderRepository.findById(param.orderId()).ifPresent(order -> {
                order.setCommentTime(java.time.OffsetDateTime.now());
                customerOrderRepository.save(order);
            });
        }

        return saved;
    }

    @Override
    public MediaUploadResponse uploadMedia(MultipartFile file) {
        try {
            var member = memberService.getCurrentMember();
            Long userId = member != null ? member.getId() : 0L;
            String originalFilename = file.getOriginalFilename();
            String ext =
                    originalFilename != null && originalFilename.contains(".")
                            ? originalFilename.substring(originalFilename.lastIndexOf("."))
                            : ".png";
            String uuid = UUID.randomUUID().toString();
            String objectName = "public/users/comments/temp_" + userId + "/" + uuid + ext;

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectName)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return new MediaUploadResponse(s3Endpoint + "/" + bucketName + "/" + objectName);
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

        String uidStr = String.valueOf(userId);
        if (Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(likeKey, uidStr))) {
            // Undo like
            stringRedisTemplate.opsForSet().remove(likeKey, uidStr);
            review.setLikeCount(Math.max(0, (review.getLikeCount() == null ? 0 : review.getLikeCount()) - 1));
        } else {
            // Do like
            stringRedisTemplate.opsForSet().add(likeKey, uidStr);
            review.setLikeCount((review.getLikeCount() == null ? 0 : review.getLikeCount()) + 1);

            // If already disliked, undo dislike
            if (Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(dislikeKey, uidStr))) {
                stringRedisTemplate.opsForSet().remove(dislikeKey, uidStr);
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

        String uidStr = String.valueOf(userId);
        if (Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(dislikeKey, uidStr))) {
            // Undo dislike
            stringRedisTemplate.opsForSet().remove(dislikeKey, uidStr);
            review.setDislikeCount(Math.max(0, (review.getDislikeCount() == null ? 0 : review.getDislikeCount()) - 1));
        } else {
            // Do dislike
            stringRedisTemplate.opsForSet().add(dislikeKey, uidStr);
            review.setDislikeCount((review.getDislikeCount() == null ? 0 : review.getDislikeCount()) + 1);

            // If already liked, undo like
            if (Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(likeKey, uidStr))) {
                stringRedisTemplate.opsForSet().remove(likeKey, uidStr);
                review.setLikeCount(Math.max(0, (review.getLikeCount() == null ? 0 : review.getLikeCount()) - 1));
            }
        }

        reviewRepository.save(review);

        return review.getDislikeCount();
    }
}
