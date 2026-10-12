package com.nexusengine.core.domain.promotion.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "flash_sale", schema = "promotion")
public class FlashSale implements Serializable {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "title", nullable = false)

private String title;

        @Column(name = "starts_on", nullable = false)

private LocalDate startDate;

        @Column(name = "ends_on", nullable = false)

private LocalDate endDate;

        @Column(name = "is_active")
    @jakarta.persistence.Convert(converter = com.nexusengine.core.domain.shared.converter.IntegerToBooleanConverter.class)
private Integer status;

        @Column(name = "created_at")
        private OffsetDateTime createTime = OffsetDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (createTime == null) {
            createTime = OffsetDateTime.now();
        }
        if (status == null) {
            status = 1;
        }
    }
}
