package com.nexusengine.core.domain.customer.entity;

import jakarta.persistence.FetchType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "member_address", schema = "customer")
public class MemberAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member member;

    @Schema(title = "Name")
    @Column(name = "recipient_name")
    private String name;

    @Column(name = "recipient_phone")
    private String phoneNumber;

    @Column(name = "is_default")
    @Schema(title = "Default status")
    private Boolean defaultStatus = false;

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
        return Boolean.TRUE.equals(defaultStatus);
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

    @PrePersist
    protected void onCreate() {
        if (defaultStatus == null) {
            defaultStatus = false;
        }
    }
}
