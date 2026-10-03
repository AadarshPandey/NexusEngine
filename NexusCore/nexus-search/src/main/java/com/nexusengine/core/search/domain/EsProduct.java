package com.nexusengine.core.search.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Represents the EsProduct component.
 * Provides core functionality and operations for EsProduct.
 */
@Data

@Document(indexName = "pms")
@Setting(shards = 1,replicas = 0)
public class EsProduct implements Serializable {
    private static final long serialVersionUID = -1L;
    @Id
    private Long id;
    @Field(type = FieldType.Text)
    private String productSn;
    private Long brandId;
    @Field(type = FieldType.Text)
    private String brandName;
    private Long productCategoryId;
    private String pic;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Text)
    private String keywords;
    private BigDecimal price;
    private Integer sale;
    private Integer newStatus;
    private Integer recommendStatus;
    private Integer stock;
    private Integer promotionType;
    @Field(type = FieldType.Nested, fielddata = true)
    private List<EsProductAttributeValue> attrValueList;
}
