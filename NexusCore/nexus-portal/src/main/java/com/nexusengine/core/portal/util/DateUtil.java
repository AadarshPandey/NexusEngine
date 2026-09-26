package com.nexusengine.core.portal.util;

import java.time.LocalDateTime;

public class DateUtil {

    public static LocalDateTime getDate(LocalDateTime date) {
        if (date == null) return null;
        return date.toLocalDate().atStartOfDay();
    }

    public static LocalDateTime getTime(LocalDateTime date) {
        if (date == null) return null;
        return LocalDateTime.of(1970, 1, 1, date.getHour(), date.getMinute(), date.getSecond(), date.getNano());
    }
}
