package com.nexusengine.core.domain.catalog.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "product_media", schema = "catalog")
public class ProductMedia {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "product_id", nullable = false)

private Long productId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @JsonBackReference

private Product product;

        @Column(name = "media_type", length = 50)

private String mediaType;

        @Column(name = "url", nullable = false)

private String mediaUrl;

        @Column(name = "display_order")

private Integer sortOrder;
}
