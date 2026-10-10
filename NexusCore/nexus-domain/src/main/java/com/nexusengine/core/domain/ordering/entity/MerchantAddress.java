package com.nexusengine.core.domain.ordering.entity;

import com.nexusengine.core.domain.shared.converter.IntegerToBooleanConverter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "merchant_address", schema = "ordering")
public class MerchantAddress implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

        @Column(name = "label")
    @Schema(title = "Address name")
    
private String addressName;

        @Column(name = "is_default_ship_from")
    @Schema(title = "Send status")
    @jakarta.persistence.Convert(converter = com.nexusengine.core.domain.shared.converter.IntegerToBooleanConverter.class)
private Integer sendStatus;

        @Column(name = "is_default_return_to")
    @Schema(title = "Receive status")
    @jakarta.persistence.Convert(converter = com.nexusengine.core.domain.shared.converter.IntegerToBooleanConverter.class)
private Integer receiveStatus;

            @Schema(title = "Name")
    @Column(name = "contact_name")
private String name;

            @Schema(title = "Phone")
    @Column(name = "contact_phone")
private String phone;

        @Schema(title = "State")
    
private String state;

        @Schema(title = "City")
    
private String city;

        @Column(name = "address_line")
    @Schema(title = "Detail address")
    
private String detailAddress;
}
