package com.nexusengine.core.domain.customer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.nexusengine.core.domain.catalog.entity.Category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "member_category_interest", schema = "customer")
public class MemberCategoryInterest {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "member_id")

private Long memberId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)

private Member member;

        @Column(name = "category_id")

private Long productCategoryId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)

private Category productCategory;
}
