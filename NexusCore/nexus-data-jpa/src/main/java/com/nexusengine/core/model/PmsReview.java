package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pms_review")
public class PmsReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "parent_id")
    @Schema(title = "Parent review ID if this is a reply")
    private Long parentId;

    @Schema(title = "Rating from 1-5")
    private Integer rating;

    @Schema(title = "Review text content")
    private String content;

    @Column(name = "like_count")
    private Integer likeCount;

    @Schema(title = "Status: 0->Pending, 1->Approved, 2->Rejected")
    private Integer status;

    @Column(name = "created_time")
    private Date createdTime;

    @OneToMany(mappedBy = "reviewId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PmsReviewMedia> mediaList;
}
