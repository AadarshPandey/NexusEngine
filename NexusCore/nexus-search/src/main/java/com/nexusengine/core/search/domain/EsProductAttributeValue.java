package com.nexusengine.core.search.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * Represents the EsProductAttributeValue component.
 * Provides core functionality and operations for EsProductAttributeValue.
 */
@Data
@EqualsAndHashCode
public class EsProductAttributeValue implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long productAttributeId;
    @Field(type = FieldType.Keyword)
    private String value;
    private Integer type;
    @Field(type=FieldType.Keyword)
    private String name;
}
