package com.nexusengine.core.domain.engagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "review_media", schema = "engagement")
public class ReviewMedia {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "review_id")

private Long reviewId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", insertable = false, updatable = false)

private ProductReview review;

        @Column(name = "media_type")
    @Schema(title = "Type: IMAGE or VIDEO")

private String mediaType;

        @Column(name = "url")

private String mediaUrl;

        @Column(name = "display_order")
        private Integer sortOrder;
}
