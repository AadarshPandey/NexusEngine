package com.nexusengine.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "pms_product_embedding")
public class PmsProductEmbedding implements Serializable {
    
    @Id
    @Column(name = "product_id")
    private Long productId;
    
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.VECTOR)
    @Column(name = "embedding", columnDefinition = "vector")
    private float[] embedding;
}
