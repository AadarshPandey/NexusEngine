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

    @Column(name = "free_freight_point")
    @Schema(title = "Free freight point")
    private BigDecimal freeFreightPoint;

    @Column(name = "comment_growth_point")
    @Schema(title = "Comment growth point")
    private Integer commentGrowthPoint;

    @Column(name = "priviledge_free_freight")
    @Schema(title = "Priviledge free freight")
    private Integer priviledgeFreeFreight;

    @Column(name = "priviledge_sign_in")
    @Schema(title = "Priviledge sign in")
    private Integer priviledgeSignIn;

    @Column(name = "priviledge_comment")
    @Schema(title = "Priviledge comment")
    private Integer priviledgeComment;

    @Column(name = "priviledge_promotion")
    @Schema(title = "Priviledge promotion")
    private Integer priviledgePromotion;

    @Column(name = "priviledge_member_price")
    @Schema(title = "Priviledge member price")
    private Integer priviledgeMemberPrice;

    @Column(name = "priviledge_birthday")
    @Schema(title = "Priviledge birthday")
    private Integer priviledgeBirthday;

    private String note;
}
