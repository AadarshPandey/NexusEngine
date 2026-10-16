package com.nexusengine.core.domain.iam.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "permission_group", schema = "iam")
public class PermissionGroup {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "created_at")
    @Schema(title = "Create time")

private OffsetDateTime createTime;

        @Schema(title = "Name")

private String name;

            @Schema(title = "Sort")
    @Column(name = "display_order")
private Integer sort;
}
