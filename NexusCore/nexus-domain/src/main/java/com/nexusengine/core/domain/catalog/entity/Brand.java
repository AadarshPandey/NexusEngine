package com.nexusengine.core.domain.catalog.entity;

import com.nexusengine.core.domain.shared.converter.IntegerToBooleanConverter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "brand", schema = "catalog")
public class Brand implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

    private String name;

        @Column(name = "is_visible")
    @jakarta.persistence.Convert(converter = com.nexusengine.core.domain.shared.converter.IntegerToBooleanConverter.class)
private Integer isVisible;

            @Schema(title = "Logo")
    @Column(name = "logo_url")
private String logo;

}
