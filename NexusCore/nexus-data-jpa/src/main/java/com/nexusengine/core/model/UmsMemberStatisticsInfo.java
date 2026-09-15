package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ums_member_statistics_info")
public class UmsMemberStatisticsInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "consume_amount")
    @Schema(title = "Consume amount")
    private BigDecimal consumeAmount;

    @Column(name = "order_count")
    @Schema(title = "Order count")
    private Integer orderCount;

    @Column(name = "coupon_count")
    @Schema(title = "Coupon count")
    private Integer couponCount;

    @Column(name = "comment_count")
    @Schema(title = "Comment count")
    private Integer commentCount;

    @Column(name = "return_order_count")
    @Schema(title = "Return order count")
    private Integer returnOrderCount;

    @Column(name = "login_count")
    @Schema(title = "Login count")
    private Integer loginCount;

    @Column(name = "attend_count")
    @Schema(title = "Attend count")
    private Integer attendCount;

    @Column(name = "fans_count")
    @Schema(title = "Fans count")
    private Integer fansCount;

    @Column(name = "saved_products_count")
    private Integer savedProductsCount;

    @Column(name = "saved_articles_count")
    private Integer savedArticlesCount;

    @Column(name = "saved_topics_count")
    private Integer savedTopicsCount;

    @Column(name = "liked_comments_count")
    private Integer likedCommentsCount;

    @Column(name = "invite_friend_count")
    private Integer inviteFriendCount;

    @Column(name = "recent_order_time")
    @Schema(title = "Recent order time")
    private Date recentOrderTime;
}
