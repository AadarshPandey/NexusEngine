package com.nexusengine.core.domain.catalog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_approval_event", schema = "catalog")
public class ProductApprovalEvent {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "product_id")

private Long productId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)

private Product product;

        @Column(name = "created_at")

private OffsetDateTime createTime;

        @Column(name = "reviewer_admin_id")
    @Schema(title = "Vertify man")

private String reviewerName;

            @Column(name = "decision")
    @JdbcTypeCode(SqlTypes.SMALLINT)
private Integer status;

            @Schema(title = "Detail")
    @Column(name = "comment")
private String detail;
}
