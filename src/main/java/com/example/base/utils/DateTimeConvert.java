package com.example.base.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import sun.util.resources.LocaleData;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author benben
 * @program base
 * @Description 日期转换类
 * @date 2021-07-17 10:46
 */
public class DateTimeConvert {
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);

    private enum FormatTypeEnum {
        DATE,
        TIME,
        DATE_TIME
    }

    private static FastDateFormat getFastDateFormat(TimeZone timeZone, String pattern, FormatTypeEnum formatTypeEnum) {
        if (StringUtils.isBlank(pattern)){
            switch (formatTypeEnum) {
                case DATE:
                    pattern = DATE_PATTERN;
                    break;
                case TIME:
                    pattern = TIME_PATTERN;
                    break;
                case DATE_TIME:
                default:
                    pattern = DATE_TIME_PATTERN;
                    break;
            }
        }
        if (timeZone == null){
            timeZone = TimeZone.getDefault();
        }
        return FastDateFormat.getInstance(pattern, timeZone);
    }

    /**
     * Java 8 日期类和 Date 互转
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDate toLocalDate(Date date, ZoneId zoneId) {
        if (zoneId == null) {
            return toLocalDate(date);
        }
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(zoneId).toLocalDate();
    }

    public static LocalTime toLocalTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    public static LocalTime toLocalTime(Date date, ZoneId zoneId) {
        if (zoneId == null) {
            return toLocalTime(date);
        }
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(zoneId).toLocalTime();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(Date date, ZoneId zoneId) {
        if (zoneId == null) {
            return toLocalDateTime(date);
        }
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(zoneId).toLocalDateTime();
    }

    public static Date toDate(LocalDate localDate) {
        if (null == localDate) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDate localDate, ZoneId zoneId) {
        if (zoneId == null) {
            return toDate(localDate);
        }
        if (null == localDate) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(zoneId).toInstant());
    }

    public static Date toDate(LocalDate localDate, LocalTime localTime) {
        if (localTime == null) {
            return toDate(localDate);
        }
        if (localDate == null) {
            return null;
        }
        return Date.from(localTime.atDate(localDate).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDate localDate, LocalTime localTime, ZoneId zoneId) {
        if (zoneId == null) {
            return toDate(localDate, localTime);
        }
        if (localTime == null) {
            return toDate(localDate, zoneId);
        }
        if (localDate == null) {
            return null;
        }
        return Date.from(localTime.atDate(localDate).atZone(zoneId).toInstant());
    }

    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDateTime localDateTime, ZoneId zoneId) {
        if (zoneId == null) {
            return toDate(localDateTime);
        }
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    /**
     * Java 8 日期类转 String
     */
    public static String toDateStr(LocalDate localeDate) {
        if (null == localeDate) {
            return null;
        }
        return localeDate.format(DATE_FORMATTER);
    }

    public static String toDateStr(LocalDate localeDate, ZoneId zoneId) {
        if (zoneId == null) {
            return toDateStr(localeDate);
        }
        if (localeDate == null) {
            return null;
        }
        return localeDate.format(DATE_FORMATTER.withZone(zoneId));
    }

    public static String toDateStr(LocalDate localeDate, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return toDateStr(localeDate);
        }
        if (localeDate == null) {
            return null;
        }
        return localeDate.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String toDateStr(LocalDate localeDate, ZoneId zoneId, String pattern) {
        if (zoneId == null) {
            return toDateStr(localeDate, pattern);
        }
        if (StringUtils.isBlank(pattern)) {
            return toDateStr(localeDate, zoneId);
        }
        if (localeDate == null) {
            return null;
        }
        return localeDate.format(DateTimeFormatter.ofPattern(pattern).withZone(zoneId));
    }

    public static String toTimeStr(LocalTime localTime) {
        if (localTime == null) {
            return null;
        }
        return localTime.format(TIME_FORMATTER);
    }

    public static String toTimeStr(LocalTime localTime, ZoneId zoneId) {
        if (zoneId == null) {
            return toTimeStr(localTime);
        }
        if (localTime == null) {
            return null;
        }
        return localTime.format(TIME_FORMATTER.withZone(zoneId));
    }

    public static String toTimeStr(LocalTime localTime, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return toTimeStr(localTime);
        }
        if (localTime == null) {
            return null;
        }
        return localTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String toTimeStr(LocalTime localTime, ZoneId zoneId, String pattern) {
        if (zoneId == null) {
            return toTimeStr(localTime, pattern);
        }
        if (StringUtils.isBlank(pattern)) {
            return toTimeStr(localTime, zoneId);
        }
        if (localTime == null) {
            return null;
        }
        return localTime.format(DateTimeFormatter.ofPattern(pattern).withZone(zoneId));
    }

    public static String toDateTimeStr(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(DATE_TIME_FORMATTER);
    }

    public static String toDateTimeStr(LocalDateTime localDateTime, ZoneId zoneId) {
        if (zoneId == null) {
            return toDateTimeStr(localDateTime);
        }
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(DATE_TIME_FORMATTER.withZone(zoneId));
    }

    public static String toDateTimeStr(LocalDateTime localDateTime, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return toDateTimeStr(localDateTime);
        }
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String toDateTimeStr(LocalDateTime localDateTime, ZoneId zoneId, String pattern) {
        if (zoneId == null) {
            return toDateTimeStr(localDateTime, pattern);
        }
        if (StringUtils.isBlank(pattern)) {
            return toDateTimeStr(localDateTime, zoneId);
        }
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern).withZone(zoneId));
    }

    /**
     * 日期类转 String
     */
    public static String toDateStr(Date date) {
        if (date == null) {
            return null;
        }
        return FastDateFormat.getInstance(DATE_PATTERN).format(date);
    }

    public static String toDateStr(Date date, TimeZone timeZone) {
        return toDateStr(date, timeZone, null);
    }

    public static String toDateStr(Date date, String pattern) {
        return toDateStr(date, null, pattern);
    }

    public static String toDateStr(Date date, TimeZone timeZone, String pattern) {
        if (date == null) {
            return null;
        }
        return getFastDateFormat(timeZone, pattern, FormatTypeEnum.DATE).format(date);
    }

    public static String toTimeStr(Date date) {
        if (date == null) {
            return null;
        }
        return toTimeStr(date, null, null);
    }

    public static String toTimeStr(Date date, TimeZone timeZone) {
        if (date == null) {
            return null;
        }
        return toTimeStr(date, timeZone, null);
    }

    public static String toTimeStr(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return toTimeStr(date, null, pattern);
    }

    public static String toTimeStr(Date date, TimeZone timeZone, String pattern) {
        if (date == null) {
            return null;
        }
        return getFastDateFormat(timeZone, pattern, FormatTypeEnum.TIME).format(date);
    }

    public static String toDateTimeStr(Date date) {
        if (date == null) {
            return null;
        }
        return toDateTimeStr(date, null, null);
    }

    public static String toDateTimeStr(Date date, TimeZone timeZone) {
        if (date == null) {
            return null;
        }
        return toDateTimeStr(date, timeZone, null);
    }

    public static String toDateTimeStr(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return toDateTimeStr(date, null, pattern);
    }

    public static String toDateTimeStr(Date date, TimeZone timeZone, String pattern) {
        if (date == null) {
            return null;
        }
        return getFastDateFormat(timeZone, pattern, FormatTypeEnum.DATE_TIME).format(date);
    }
}
