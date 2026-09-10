package com.nexusengine.core.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility for extracting client IP addresses from HTTP requests.
 */
public class RequestUtil {

    private static final Logger log = LoggerFactory.getLogger(RequestUtil.class);

    /**
     * Returns the client IP address.
     * Uses remoteAddr for security — X-Forwarded-For headers are client-controlled
     * and should only be trusted when the app sits behind a known reverse proxy
     * configured to overwrite (not append to) the header.
     */
    public static String getRequestIp(HttpServletRequest request) {
        return request.getRemoteAddr();
    }
}
