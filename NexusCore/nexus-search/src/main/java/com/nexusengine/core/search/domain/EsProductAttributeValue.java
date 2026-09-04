package com.nexusengine.core.search.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * Auto-generated documentation
 * Created by macro on 2018/6/27.
 */
@Data
@EqualsAndHashCode
public class EsProductAttributeValue implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long productAttributeId;
    // Auto-generated documentation
    @Field(type = FieldType.Keyword)
    private String value;
    // Auto-generated documentation
    private Integer type;
    // Auto-generated documentation
    @Field(type=FieldType.Keyword)
    private String name;
}
