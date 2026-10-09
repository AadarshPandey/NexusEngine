package com.nexusengine.core.model;

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
@Table(name = "permission_group", schema = "iam")
public class UmsResourceCategory implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

        @Column(name = "created_at")
    @Schema(title = "Create time")
    
private OffsetDateTime createTime;

        @Schema(title = "Name")
    
private String name;

            @Schema(title = "Sort")
    @Column(name = "display_order")
private Integer sort;
}
