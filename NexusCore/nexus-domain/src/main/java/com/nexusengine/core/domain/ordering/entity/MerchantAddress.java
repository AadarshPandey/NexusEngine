package com.nexusengine.core.domain.ordering.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "merchant_address", schema = "ordering")
public class MerchantAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "label")
    @Schema(title = "Address name")
    private String addressName;

    @Column(name = "is_default_ship_from")
    @Schema(title = "Send status")
    private Boolean sendStatus;

    @Column(name = "is_default_return_to")
    @Schema(title = "Receive status")
    private Boolean receiveStatus;

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
