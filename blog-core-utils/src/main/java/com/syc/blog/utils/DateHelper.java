package com.syc.blog.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateHelper {

    /**
     * 获取指定日期
     * */
    public static String getDateStr(Date date, String format){
        if (date == null)
            return null;
        return new SimpleDateFormat(format).format(date);
    }

    public static Date getCurrentDateZero(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    public static Date getCurrentDateEnd(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取当前日期
     * */
    public static String getCurrentDateStr(String format){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    /**
     * 获取本周周一
     * */
    public static Date getThisWeekMonday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    /**
     * 获取本周周日
     * */
    public static Date getThisWeekSunday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.add(Calendar.DATE, 6);
        return cal.getTime();
    }

    /**
     * 获取指定天数之后的日期
     * */
    public static String getFutureDateByDay(String format,Integer day) throws ParseException {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.add(Calendar.DAY_OF_YEAR, day);
        return getDateStr(calendar.getTime(), format);
    }

    /**
     * 获取指定月数之后的日期
     * */
    public static String getFutureDateByMonth(String format,Integer month) throws ParseException {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.add(Calendar.MONTH, month);
        return getDateStr(calendar.getTime(), format);
    }


    /**
     * 得到本月的第一天
     * @return
     */
    public static Date getMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 得到本月的最后一天
     * @return
     */
    public static Date getMonthLastDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }


    /**
     * 获取指定天数前后某一天
     * */
    public static Date getDayRangeBySpecific(String time,int n){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date=null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int day=calendar.get(Calendar.DATE);
        // 后一天为 +1   前一天 为-1
        calendar.set(Calendar.DATE,day+n);

        return calendar.getTime();
    }


    public static void main(String[] args) {
        String dateStr = getDateStr(new Date(), "yyyy-MM-dd");
        System.out.println(dateStr);
        if(dateStr.compareTo("") > 0){

        }
        Date dayRangeBySpecific1= getDayRangeBySpecific("2020-01-24", -30);
        Date dayRangeBySpecific2 = getDayRangeBySpecific("2020-01-24", 30);
        if(new Date().compareTo(dayRangeBySpecific1) > 0 && new Date().compareTo(dayRangeBySpecific2) < 0){
            System.out.println(1);
        }
    }
}
