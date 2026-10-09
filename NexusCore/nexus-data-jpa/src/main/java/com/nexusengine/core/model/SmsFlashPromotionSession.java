package com.nexusengine.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "flash_sale_slot", schema = "promotion")
public class SmsFlashPromotionSession implements Serializable {
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
    @jakarta.persistence.Convert(converter = com.nexusengine.core.model.converter.IntegerToBooleanConverter.class)
private Integer status;

        @Column(name = "created_at")
    
private OffsetDateTime createTime;
}
