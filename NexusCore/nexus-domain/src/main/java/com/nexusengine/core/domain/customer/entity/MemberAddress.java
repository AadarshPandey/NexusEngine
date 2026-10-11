package com.nexusengine.core.domain.customer.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "member_address", schema = "customer")
public class MemberAddress implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "member_id")

private Long memberId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "member_id", insertable = false, updatable = false)

private Member member;

            @Schema(title = "Name")
    @Column(name = "recipient_name")
private String name;

        @Column(name = "recipient_phone")

private String phoneNumber;

        @Column(name = "is_default")
    @Schema(title = "Default status")
    @jakarta.persistence.Convert(converter = com.nexusengine.core.domain.shared.converter.IntegerToBooleanConverter.class)
private Integer defaultStatus;

        @Column(name = "postal_code")
    @Schema(title = "Post code")

private String postCode;

            @Schema(title = "State")
    @Column(name = "state")
private String state;

        @Schema(title = "City")

private String city;

            @Schema(title = "Region")
    @Column(name = "district")
private String region;

        @Column(name = "address_line")
    @Schema(title = "Detail address")
    private String detailAddress;

    public String getRecipientName() {
        return name;
    }

    public String getRecipientPhone() {
        return phoneNumber;
    }

    public Boolean getIsDefault() {
        return defaultStatus != null && defaultStatus == 1;
    }

    public String getAddressLine() {
        return detailAddress;
    }

    public String getPostalCode() {
        return postCode;
    }

    public String getDistrict() {
        return region;
    }
}
