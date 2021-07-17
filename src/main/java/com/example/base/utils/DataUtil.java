package com.example.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author benben
 * @program base
 * @Description 日期工具类
 * @date 2021-03-22 15:50
 */
public class DataUtil {

    private static final String dateFormatToMonth = "yyyy-MM";

    private static final String dateFormatToDay = "yyyy-MM-dd";

    private static final String dateFormatToYear = "yyyy";

    private static final String dateToDay = "yyyyMMdd";

    private static final String dateHourToSec = "HH:mm:ss";

    private static final String dateHourToMin = "HH:mm";

    private static final String dateFormatToMin = "yyyy-MM-dd HH:mm";

    private static final String dateFormatToSec = "yyyy-MM-dd HH:mm:ss";

    private static final String dateExactToDay = "MM月dd日";


    /**
     * 判断当前时间是不是在该时间范围内
     *
     * @param startTime startTime
     * @param endTime   endTime
     * @return boolean
     */
    public static boolean isEffectiveDate(Date startTime, Date endTime) {

        Date date = new Date();

        if (date.getTime() == startTime.getTime() || date.getTime() == endTime.getTime()) return true;

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(startTime);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endTime);

        return calendarDate.after(beginCalendar) && calendarDate.before(endCalendar);
    }

    /**
     * 字符串类型转日期
     *
     * @param date date yyyy-MM-dd
     * @return date
     */
    public static Date getDateToDay(String date) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormatToDay);

        Date date1 = null;
        try {
            date1 = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    /**
     * 字符串类型转日期-月
     *
     * @param date date yyyy-MM-dd
     * @return date
     */
    public static Date getDateToDayMonth(String date) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormatToMonth);

        Date date1 = null;
        try {
            date1 = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    /**
     * 字符串类型转日期
     *
     * @param date date yyyy-MM-dd
     * @return date
     */
    public static Date getDateToDaySec(String date) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormatToSec);

        Date date1 = null;
        try {
            date1 = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return date1;
    }

    /**
     * 将日期装换成字符串
     *
     * @param date date
     * @return date
     */
    public static String getDateStrToDaySec(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormatToSec);
        return format.format(date);
    }

    /**
     * 将日期装换成字符串
     *
     * @param date date
     * @return date
     */
    public static String getDateStrToDay(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormatToYear);
        return format.format(date);
    }

    /**
     * 获取某年第一天日期
     *
     * @return Date
     */
    public static Date getCurrYearFirst(String dateStrToDay) {
        Calendar currCal = Calendar.getInstance();
        currCal.setTime(getDateToYear(dateStrToDay));
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    private static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 字符串类型转日期
     *
     * @param date yyyy-MM-dd
     * @return date
     */
    private static Date getDateToYear(String date) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormatToYear);
        Date date1 = null;
        try {
            date1 = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date1;
    }

    /**
     * 获取当前年的上一年的开始时间
     *
     * @return date
     */
    public static Date getLastYearStartDate() {
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.YEAR, -1);
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatToYear);
        String lastTimeYear = sdf.format(c1.getTime());
        String first = lastTimeYear + "-01-01 00:00:00";

        return getDateToDaySec(first);
    }

    /**
     * 获取当前年的上一年的结束时间
     *
     * @return date
     */
    public static Date getendYearEndDate() {
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.YEAR, -1);
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatToYear);
        String lastTimeYear = sdf.format(c1.getTime());
        String last = lastTimeYear + "-12-31 23:59:59";
        return getDateToDaySec(last);
    }

    /**
     * 获取当年的开始时间戳
     *
     * @return Date
     */
    public static Date getYearStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.YEAR));
        return cal.getTime();
    }


    /**
     * 获取时间所在月第一天和最后一天 字符串
     *
     * @param start_time start_time
     * @param num        0-第一天  1-最后一天
     * @return String
     */
    public static String getEndDayOrOneDay(Date start_time, int num) {
        String time;
        Calendar cale;
        SimpleDateFormat format = new SimpleDateFormat(dateFormatToDay);
        if (num == 0) {
            // 获取当前月的第一天
            cale = Calendar.getInstance();
            cale.setTime(start_time);
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            time = format.format(cale.getTime()) + " 00:00:00";
        } else {
            // 获取当前月的最后一天
            cale = Calendar.getInstance();
            cale.setTime(start_time);
            cale.add(Calendar.MONTH, 1);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            time = format.format(cale.getTime()) + " 23:59:59";
        }
        return time;
    }
}
