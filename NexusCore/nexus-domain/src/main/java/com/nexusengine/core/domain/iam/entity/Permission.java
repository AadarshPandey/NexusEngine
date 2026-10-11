package com.nexusengine.core.domain.iam.entity;

import com.nexusengine.core.domain.catalog.entity.Category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "permission", schema = "iam")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "created_at")
    @Schema(title = "Create time")

private OffsetDateTime createTime;

        @Schema(title = "Name")

private String name;

            @Schema(title = "Url")
    @Column(name = "url_pattern")
private String url;

        @Schema(title = "Description")

private String description;

        @Column(name = "permission_group_id")
    @Schema(title = "Category id")

private Long categoryId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "permission_group_id", insertable = false, updatable = false)

private Category category;
}
