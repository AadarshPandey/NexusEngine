package com.nexusengine.core.domain.catalog.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_approval_event", schema = "catalog")
public class ProductApprovalEvent implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

        @Column(name = "product_id")
    
private Long productId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "product_id", insertable = false, updatable = false)
    
private Product product;

        @Column(name = "created_at")
    
private OffsetDateTime createTime;

        @Column(name = "reviewer_admin_id")
    @Schema(title = "Vertify man")
    
private String reviewerName;

            @Column(name = "decision")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer status;

            @Schema(title = "Detail")
    @Column(name = "comment")
private String detail;
}
