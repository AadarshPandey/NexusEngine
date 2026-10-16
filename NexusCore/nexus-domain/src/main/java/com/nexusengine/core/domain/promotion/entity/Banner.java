package com.nexusengine.core.domain.promotion.entity;

import org.hibernate.type.SqlTypes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "banner", schema = "promotion")
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String name;

    @Schema(title = "Type")
    @Column(name = "placement")
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private Integer type;

    @Column(name = "image_url")
    private String primaryImageUrl;

    @Column(name = "starts_at")
    private OffsetDateTime startsAt;

    @Column(name = "ends_at")
    private OffsetDateTime endsAt;

    @Schema(title = "Status")
    @Column(name = "is_active")
    private Boolean status;

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

    @Schema(title = "Display order")
    @Column(name = "display_order")
    private Integer displayOrder;

    public String getTitle() {
        return name;
    }

    public String getTargetUrl() {
        return url;
    }
}
