package com.nexusengine.core.domain.engagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.customer.entity.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_review", schema = "engagement")
public class ProductReview {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "product_id")

private Long productId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)

private Product product;

        @Column(name = "member_id")

private Long memberId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)

private Member member;

        @Schema(title = "Rating from 1-5")
    @JdbcTypeCode(SqlTypes.SMALLINT)
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
    @JdbcTypeCode(SqlTypes.SMALLINT)
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

    public String getAuthorDisplayName() {
        return member != null ? (member.getDisplayName() != null ? member.getDisplayName() : member.getUsername()) : null;
    }

    public String getAuthorAvatarUrl() {
        return member != null ? member.getAvatarUrl() : null;
    }

    public String getMemberNickName() {
        return getAuthorDisplayName();
    }

    public String getBody() {
        return content;
    }

    public void setBody(String body) {
        this.content = body;
    }

    public Integer getModerationStatus() {
        return status;
    }

    public void setModerationStatus(Integer moderationStatus) {
        this.status = moderationStatus;
    }

    public OffsetDateTime getCreatedAt() {
        return createdTime;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdTime = createdAt;
    }

    public Integer getHelpfulCount() {
        return likeCount;
    }

    public void setHelpfulCount(Integer helpfulCount) {
        this.likeCount = helpfulCount;
    }
}
