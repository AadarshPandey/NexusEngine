package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pms_brand")
public class PmsBrand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "first_letter")
    @Schema(title = "First letter")
    private String firstLetter;

    private Integer sort;

    @Column(name = "factory_status")
    @Schema(title = "Factory status")
    private Integer factoryStatus;

    @Column(name = "show_status")
    private Integer showStatus;

    @Column(name = "product_count")
    @Schema(title = "Product count")
    private Integer productCount;

    @Column(name = "product_comment_count")
    @Schema(title = "Product comment count")
    private Integer productCommentCount;

    @Schema(title = "Logo")
    private String logo;

    @Column(name = "big_pic")
    @Schema(title = "Big pic")
    private String bigPic;

    @Column(name = "brand_story")
    @Schema(title = "Brand story")
    private String brandStory;
}
