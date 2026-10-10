package com.nexusengine.core.domain.iam.entity;

import com.nexusengine.core.domain.shared.converter.IntegerToBooleanConverter;

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
@Table(name = "role", schema = "iam")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

        @Schema(title = "Name")
    
private String name;

        @Schema(title = "Description")
    
private String description;

            @org.hibernate.annotations.Formula("(SELECT COUNT(*) FROM iam.admin_user_role r WHERE r.role_id = id)")
    private Integer adminCount;

        @Column(name = "created_at")
    @Schema(title = "Create time")
    
private OffsetDateTime createTime;

            @Schema(title = "Status")
    @Column(name = "is_active")
    @jakarta.persistence.Convert(converter = com.nexusengine.core.domain.shared.converter.IntegerToBooleanConverter.class)
private Integer status;

            @Column(name = "display_order")
private Integer sort;
}
