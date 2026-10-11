package com.nexusengine.core.domain.promotion.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "banner", schema = "promotion")
public class Banner implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

            @Column(name = "title")
private String name;

            @Schema(title = "Type")
    @Column(name = "placement")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)
private Integer type;

    @com.fasterxml.jackson.annotation.JsonProperty("primaryImageUrl")
            @Column(name = "image_url")
private String primaryImageUrl;

        @Column(name = "starts_at")

@com.fasterxml.jackson.annotation.JsonProperty("startsAt")
    private OffsetDateTime startTime;

        @Column(name = "ends_at")

@com.fasterxml.jackson.annotation.JsonProperty("endsAt")
    private OffsetDateTime endTime;

            @Schema(title = "Status")
    @Column(name = "is_active")
    @jakarta.persistence.Convert(converter = com.nexusengine.core.domain.shared.converter.IntegerToBooleanConverter.class)
private Integer status;

        @Column(name = "click_count")
    @Schema(title = "Click count")

private Integer clickCount;

        @Column(name = "order_count")
    @Schema(title = "Order count")

private Integer orderCount;

            @Schema(title = "Url")
    @Column(name = "target_url")
private String url;

            @Schema(title = "Note")
    @Column(name = "internal_note")
private String note;

            @Schema(title = "Sort")
    @Column(name = "display_order")
    @com.fasterxml.jackson.annotation.JsonProperty("displayOrder")
    private Integer sort;

    public String getTitle() {
        return name;
    }

    public String getTargetUrl() {
        return url;
    }
}
