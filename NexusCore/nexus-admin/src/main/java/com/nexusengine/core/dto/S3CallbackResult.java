package com.nexusengine.core.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * S3 upload callback result
 */
@Getter
@Setter
public class S3CallbackResult {
    private String filename;
    private String size;
    private String mimeType;
    private String url;
}
