package com.nexusengine.core.domain.promotion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "flash_sale", schema = "promotion")
public class FlashSale {

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
    private Boolean status;

    @Column(name = "created_at")
    private OffsetDateTime createTime = OffsetDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (createTime == null) {
            createTime = OffsetDateTime.now();
        }
        if (status == null) {
            status = true;
        }
    }
}
