package com.nexusengine.core.domain.iam.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

@Getter
@Setter
@Entity
@Table(name = "role", schema = "iam")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(title = "Name")
    private String name;

    @Schema(title = "Description")
    private String description;

    @Formula("(SELECT COUNT(*) FROM iam.admin_user_role r WHERE r.role_id = id)")
    private Integer adminCount;

    @Column(name = "created_at")
    @Schema(title = "Create time")
    private OffsetDateTime createTime;

    @Schema(title = "Status")
    @Column(name = "is_active")
    private Boolean status;

    @Column(name = "display_order")
    private Integer sort;
}
