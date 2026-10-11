package com.nexusengine.core.domain.catalog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "product_media", schema = "catalog")
public class ProductMedia implements Serializable {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "product_id", nullable = false)

private Long productId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "product_id", insertable = false, updatable = false)
    @com.fasterxml.jackson.annotation.JsonBackReference

private Product product;

        @Column(name = "media_type", length = 50)

private String mediaType;

        @Column(name = "url", nullable = false)

private String mediaUrl;

        @Column(name = "display_order")

private Integer sortOrder;
}
