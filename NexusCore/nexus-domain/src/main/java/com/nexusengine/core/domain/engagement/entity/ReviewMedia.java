package com.nexusengine.core.domain.engagement.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "review_media", schema = "engagement")
public class ReviewMedia implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

        @Column(name = "review_id")
    
private Long reviewId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "review_id", insertable = false, updatable = false)
    
private ProductReview review;

        @Column(name = "media_type")
    @Schema(title = "Type: IMAGE or VIDEO")
    
private String mediaType;

        @Column(name = "url")
    
private String mediaUrl;

        @Column(name = "thumbnail_url")
    
private String thumbnailUrl;

        @Column(name = "display_order")
    
private Integer sortOrder;
}
