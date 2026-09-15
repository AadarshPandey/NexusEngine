package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ums_member_task")
public class UmsMemberTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Schema(title = "Growth")
    private Integer experiencePoints;

    @Schema(title = "RewardPoints")
    private Integer rewardPoints;

    @Schema(title = "Type")
    private Integer type;
}
