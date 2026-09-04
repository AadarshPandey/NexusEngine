package com.nexusengine.core.search.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/6/27.
 */
@Data
@EqualsAndHashCode
public class EsProductRelatedInfo {
    private List<String> brandNames;
    private List<String> productCategoryNames;
    private List<ProductAttr> productAttrs;

    @Data
    @EqualsAndHashCode
    public static class ProductAttr {
        private Long attrId;
        private String attrName;
        private List<String> attrValues;
    }
}
