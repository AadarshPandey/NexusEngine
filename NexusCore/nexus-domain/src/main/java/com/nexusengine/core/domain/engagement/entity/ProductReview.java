package com.nexusengine.core.domain.engagement.entity;

import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.customer.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_review", schema = "engagement")
public class ProductReview implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "product_id")

private Long productId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "product_id", insertable = false, updatable = false)

private Product product;

        @Column(name = "member_id")

private Long memberId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "member_id", insertable = false, updatable = false)

private Member member;

        @Column(name = "parent_review_id")
    @Schema(title = "Parent review ID if this is a reply")

private Long parentId;

        @Schema(title = "Rating from 1-5")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer rating;

            @Schema(title = "Review text content")
    @Column(name = "body")
private String content;

        @Column(name = "helpful_count")

private Integer likeCount;

        @Column(name = "not_helpful_count")

private Integer dislikeCount;

            @Schema(title = "Status: 0->Pending, 1->Approved, 2->Rejected")
    @Column(name = "moderation_status")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer status;

        @Column(name = "created_at")

private OffsetDateTime createdTime;

        @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")

private List<ReviewMedia> attachmentList;

        @Transient

private Boolean userLiked;

        @Transient

private Boolean userDisliked;
}
