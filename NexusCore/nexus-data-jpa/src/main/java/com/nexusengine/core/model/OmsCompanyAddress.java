package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "oms_company_address")
public class OmsCompanyAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address_name")
    @Schema(title = "Address name")
    private String addressName;

    @Column(name = "send_status")
    @Schema(title = "Send status")
    private Integer sendStatus;

    @Column(name = "receive_status")
    @Schema(title = "Receive status")
    private Integer receiveStatus;

    @Schema(title = "Name")
    private String name;

    @Schema(title = "Phone")
    private String phone;

    @Schema(title = "Province")
    private String province;

    @Schema(title = "City")
    private String city;

    @Schema(title = "Region")
    private String region;

    @Column(name = "detail_address")
    @Schema(title = "Detail address")
    private String detailAddress;
}
