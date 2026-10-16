package com.nexusengine.core.domain.promotion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "flash_sale_slot", schema = "promotion")
public class FlashSaleSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "label", nullable = false)
    private String name;

    @Column(name = "starts_at_local", nullable = false)
    private LocalTime startTime;

    @Column(name = "ends_at_local", nullable = false)
    private LocalTime endTime;

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
