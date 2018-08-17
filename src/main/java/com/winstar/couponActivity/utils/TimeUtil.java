package com.winstar.couponActivity.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * TimeUtil
 *
 * @author: Big BB
 * @create 2018-03-20 17:06
 * @DESCRIPTION:
 **/
public class TimeUtil {


    public static Date getStringToDate(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(time);
        return date;
    }

    /**
     * 获取七天后的时间
     * @param currentTime：yyyy-MM-dd
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static Date getSenverDay(String currentTime){
        Date d=new Date();
        Date data=null;
        SimpleDateFormat formatMonth=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatHour=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String validBeginAt=formatMonth.format(new Date(d.getTime()+ (long)6 * 24 * 60 * 60 * 1000))+" 23:59:59";
        try {
            data=formatHour.parse(validBeginAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }
    /**
     * 计算2个日期之间相差的  相差多少年月日
     * 比如：2011-02-02 到  2017-03-02 相差 6年，1个月，0天
     * @param fromDate
     * @param toDate
     * @return
     */
    public static String  dayComparePrecise(Date fromDate,Date toDate){
        Calendar  from  =  Calendar.getInstance();
        from.setTime(fromDate);
        Calendar  to  =  Calendar.getInstance();
        to.setTime(toDate);
        int fromYear = from.get(Calendar.YEAR);
        int fromMonth = from.get(Calendar.MONTH);
        int fromDay = from.get(Calendar.DAY_OF_MONTH);
        int toYear = to.get(Calendar.YEAR);
        int toMonth = to.get(Calendar.MONTH);
        int toDay = to.get(Calendar.DAY_OF_MONTH);
        double  year = toYear  -  fromYear;
        double  month = toMonth  - fromMonth;
        int day = toDay  - fromDay;
        DecimalFormat df = new DecimalFormat("######0.0");
        return df.format(year + month / 12);
    }
    /**
     * 计算2个日期差得到判断
     * @param currentTime
     * @param endTime
     * @return
     */
    public static Boolean  dayComparePrecise2(Date currentTime,Date endTime){
        long ts1=endTime.getTime()-currentTime.getTime();
        if(ts1>0) {
            return true;
        }else {
            return false;
        }
    }
    public static Date getMonthStart(){
        //规定返回日期格式
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        Date theDate=calendar.getTime();
        GregorianCalendar gcLast=(GregorianCalendar)Calendar.getInstance();
        gcLast.setTime(theDate);
        //设置为第一天
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        Date dayFirst= null;
        try {
            dayFirst = sf.parse(sf.format(gcLast.getTime())+" 00:00:01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayFirst;
    }
    public static Date getMonthLast(){
        //获取Calendar
        Calendar calendar = Calendar.getInstance();
        //设置日期为本月最大日期
        calendar.set(Calendar.DATE, calendar.getActualMaximum(calendar.DATE));
        //设置日期格式
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date dayLast = null;
        try {
            dayLast = sf.parse(sf.format(calendar.getTime())+" 23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayLast;
    }

    /**
     * 获取当前月
     * @return
     */
    public static String getMonth(){
        LocalDate today = LocalDate.now();
        return today.toString().substring(0,7);
    }

    public  static  String getCheckTimeNextMonth(String input) throws ParseException {
        Date checkTime = getStringYearMonth(input);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkTime);
        calendar.add(Calendar.MONTH, 1);
        return  getDateYearMonth(calendar.getTime());
    }

    public static Date getStringYearMonth(String input) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        return formatter.parse(input);
    }
    public static String getDateYearMonth(Date input) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        return formatter.format(input);
    }

    /**
     * 获取下个月的当天
     * @return
     */
    public static Date getNextMonth(){
        LocalDateTime localDateTime =LocalDateTime.now();
        System.out.println(localDateTime);
        LocalDateTime nextMonthDateTime =localDateTime.plusMonths(1);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = nextMonthDateTime.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /**
     * 获取第二年的当天
     * @return
     */
    public static Date getNextYear(){
        LocalDateTime localDateTime =LocalDateTime.now();
        System.out.println(localDateTime);
        LocalDateTime nextMonthDateTime =localDateTime.plusYears(1);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = nextMonthDateTime.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    // 默认时间格式
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = TimeFormat.SHORT_DATE_PATTERN_LINE.formatter;


    /**
     * String 转化为 LocalDateTime
     *
     * @param timeStr
     *            被转化的字符串
     * @return LocalDateTime
     */
    public static LocalDateTime parseTime(String timeStr) {
        return LocalDateTime.parse(timeStr, DEFAULT_DATETIME_FORMATTER);

    }

    /**
     * String 转化为 LocalDateTime
     *
     * @param timeStr
     *            被转化的字符串
     * @param timeFormat
     *            转化的时间格式
     * @return LocalDateTime
     */
    public static LocalDateTime parseTime(String timeStr, TimeFormat timeFormat) {
        return LocalDateTime.parse(timeStr, timeFormat.formatter);

    }

    /**
     * LocalDateTime 转化为String
     *
     * @param time
     *            LocalDateTime
     * @return String
     */
    public static String parseTime(LocalDateTime time) {
        return DEFAULT_DATETIME_FORMATTER.format(time);

    }

    /**
     * LocalDateTime 时间转 String
     *
     * @param time
     *            LocalDateTime
     * @param format
     *            时间格式
     * @return String
     */
    public static String parseTime(LocalDateTime time, TimeFormat format) {
        return format.formatter.format(time);

    }

    /**
     * 获取当前时间String:yyyy-MM-dd
     *
     * @return
     */
    public static String getCurrentDateTime() {
        return DEFAULT_DATETIME_FORMATTER.format(LocalDateTime.now());
    }
    /**
     * 获取当前时间Date格式:Date格式yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static Date getCurrentDateTime2() {
        Date data=null;
        SimpleDateFormat formatHour=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            data=formatHour.parse(TimeFormat.LONG_DATE_PATTERN_LINE.formatter.format(LocalDateTime.now()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }
    /**
     * 获取当前时间
     *
     * @param timeFormat
     *            时间格式
     * @return
     */
    public static String getCurrentDateTime(TimeFormat timeFormat) {
        return timeFormat.formatter.format(LocalDateTime.now());
    }
    public enum  TimeFormat {
        //短时间格式 年月日
        /**
         * 时间格式：yyyy-MM-dd
         */
        SHORT_DATE_PATTERN_LINE("yyyy-MM-dd"),
        /**
         * 时间格式：yyyy/MM/dd
         */
        SHORT_DATE_PATTERN_SLASH("yyyy/MM/dd"),
        /**
         * 时间格式：yyyy\\MM\\dd
         */
        SHORT_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd"),
        /**
         * 时间格式：yyyyMMdd
         */
        SHORT_DATE_PATTERN_NONE("yyyyMMdd"),

        // 长时间格式 年月日时分秒
        /**
         * 时间格式：yyyy-MM-dd HH:mm:ss
         */
        LONG_DATE_PATTERN_LINE("yyyy-MM-dd HH:mm:ss"),

        /**
         * 时间格式：yyyy/MM/dd HH:mm:ss
         */
        LONG_DATE_PATTERN_SLASH("yyyy/MM/dd HH:mm:ss"),
        /**
         * 时间格式：yyyy\\MM\\dd HH:mm:ss
         */
        LONG_DATE_PATTERN_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss"),
        /**
         * 时间格式：yyyyMMdd HH:mm:ss
         */
        LONG_DATE_PATTERN_NONE("yyyyMMdd HH:mm:ss"),

        // 长时间格式 年月日时分秒 带毫秒
        LONG_DATE_PATTERN_WITH_MILSEC_LINE("yyyy-MM-dd HH:mm:ss.SSS"),

        LONG_DATE_PATTERN_WITH_MILSEC_SLASH("yyyy/MM/dd HH:mm:ss.SSS"),

        LONG_DATE_PATTERN_WITH_MILSEC_DOUBLE_SLASH("yyyy\\MM\\dd HH:mm:ss.SSS"),

        LONG_DATE_PATTERN_WITH_MILSEC_NONE("yyyyMMdd HH:mm:ss.SSS");

        private transient DateTimeFormatter formatter;

        TimeFormat(String pattern) {
            formatter = DateTimeFormatter.ofPattern(pattern);

        }
    }
}
