package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ums_member_receive_address")
public class UmsMemberReceiveAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Schema(title = "Name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "default_status")
    @Schema(title = "Default status")
    private Integer defaultStatus;

    @Column(name = "post_code")
    @Schema(title = "Post code")
    private String postCode;

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
