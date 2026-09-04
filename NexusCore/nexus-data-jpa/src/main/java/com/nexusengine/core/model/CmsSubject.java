package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cms_subject")
public class CmsSubject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id")
    private Long categoryId;

    private String title;

    @Schema(title = "Pic")
    private String pic;

    @Column(name = "product_count")
    @Schema(title = "Product count")
    private Integer productCount;

    @Column(name = "recommend_status")
    private Integer recommendStatus;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "collect_count")
    private Integer collectCount;

    @Column(name = "read_count")
    private Integer readCount;

    @Column(name = "comment_count")
    private Integer commentCount;

    @Column(name = "album_pics")
    @Schema(title = "Album pics")
    private String albumPics;

    private String description;

    @Column(name = "show_status")
    @Schema(title = "Show status")
    private Integer showStatus;

    @Column(name = "forward_count")
    @Schema(title = "Forward count")
    private Integer forwardCount;

    @Column(name = "category_name")
    @Schema(title = "Category name")
    private String categoryName;

    private String content;
}
