package com.nexusengine.core.portal.domain;

import lombok.Data;
import java.util.List;

@Data
public class ReviewParam {
    private Long productId;
    private Long parentId;
    private Integer rating;
    private String content;
    private List<MediaParam> mediaList;

    @Data
    public static class MediaParam {
        private String mediaType;
        private String mediaUrl;
        private Integer sortOrder;
    }
}
