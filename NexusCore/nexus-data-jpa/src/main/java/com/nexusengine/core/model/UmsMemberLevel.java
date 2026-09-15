package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ums_member_level")
public class UmsMemberLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "growth_point")
    private Integer growthPoint;

    @Column(name = "default_status")
    @Schema(title = "Default status")
    private Integer defaultStatus;

    @Column(name = "free_shipping_threshold")
    @Schema(title = "Free freight point")
    private BigDecimal freeShippingThreshold;

    @Column(name = "review_reward_xp")
    @Schema(title = "Comment growth point")
    private Integer reviewRewardXp;

    @Column(name = "has_free_shipping_perk")
    @Schema(title = "Priviledge free freight")
    private Integer hasFreeShippingPerk;

    @Column(name = "can_earn_login_rewards")
    @Schema(title = "Priviledge sign in")
    private Integer canEarnLoginRewards;

    @Column(name = "has_review_privilege")
    @Schema(title = "Priviledge comment")
    private Integer hasReviewPrivilege;

    @Column(name = "has_promotion_privilege")
    @Schema(title = "Priviledge promotion")
    private Integer hasPromotionPrivilege;

    @Column(name = "has_vip_pricing")
    @Schema(title = "Priviledge member price")
    private Integer hasVipPricing;

    @Column(name = "has_birthday_privilege")
    @Schema(title = "Priviledge birthday")
    private Integer hasBirthdayPrivilege;

    private String note;
}
