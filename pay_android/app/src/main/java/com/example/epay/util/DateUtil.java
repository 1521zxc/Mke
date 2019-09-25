package com.example.epay.util;

import android.text.format.Time;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by liujin on 2018/1/17.
 */
public class DateUtil {
    /*
     * 格式化时间
     */
    public static String format(long d, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date(d * 1000);
        return dateFormat.format(date);
    }

    /*
     * 格式化时间
     */
    public static String format2(long d, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date(d);

        return dateFormat.format(date);
    }

    /*
 * 格式化时间
 */
    public static String getTime(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }


    /*
    * 格式化时间
    */
    public static long format(String time, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        long d = 0;
        try {
            Date date = dateFormat.parse(time);
            d=date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }


	/*
     *时间戳变成
	 */

    public static String getday(long d, String format) {
        String i = format(d / 1000, format);
        Calendar cal = new GregorianCalendar();
        int day, year, month = 0;
        if (format.contains("mm")) {
            year = Integer.parseInt(i.split(" ")[0].split("-")[0]);
            month = Integer.parseInt(i.split(" ")[0].split("-")[1]);
            day = Integer.parseInt(i.split(" ")[0].split("-")[2]);
        } else {
            year = Integer.parseInt(i.split("-")[0]);
            month = Integer.parseInt(i.split("-")[1]);
            day = Integer.parseInt(i.split("-")[2]);
        }
        cal.set(year, month - 1, day);
        cal.set(Calendar.MILLISECOND, 0);
        String week = "";
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return i + " " + week;
    }

    public static String getday2(long d, String format) {
        String i = format(d / 1000, format);
        Calendar cal = new GregorianCalendar();
        int day, year, month = 0;
        if (format.contains("mm")) {
            year = Integer.parseInt(i.split(" ")[0].split("-")[0]);
            month = Integer.parseInt(i.split(" ")[0].split("-")[1]);
            day = Integer.parseInt(i.split(" ")[0].split("-")[2]);
        } else {
            year = Integer.parseInt(i.split("-")[0]);
            month = Integer.parseInt(i.split("-")[1]);
            day = Integer.parseInt(i.split("-")[2]);
        }
        cal.set(year, month - 1, day);
        cal.set(Calendar.MILLISECOND, 0);
        String week = "";
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                week = "周日";
                break;
            case 2:
                week = "周一";
                break;
            case 3:
                week = "周二";
                break;
            case 4:
                week = "周三";
                break;
            case 5:
                week = "周四";
                break;
            case 6:
                week = "周五";
                break;
            case 7:
                week = "周六";
                break;
        }
        return i + " " + week;
    }

    // 将字符串转为时间戳
    public static String getTime(String user_time, String format) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d;

        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return re_time;
    }

    //-1   结束   0进行   1 未开始
    public static int stateTime(long startTime, long endTime) {
        long currentSeconds = System.currentTimeMillis();
        if ((currentSeconds - startTime) < 0) {
            return 1;
        } else {
            if ((currentSeconds - endTime) < 0) {
                return 0;
            } else {
                return -1;
            }
        }
    }


    public static Date format(String time) {
        return null;
    }

    /*
     * 是否过期
     */
    public static String converTime(long timestamp) {
        long currentSeconds = System.currentTimeMillis();
        long timeGap = (currentSeconds - timestamp) / 1000;// 与现在时间相差秒数
        String timeStr = null;
        if (timeGap > 24 * 60 * 60) {// 1天以上
            timeStr = timeGap / (24 * 60 * 60) + "天前";
        } else if (timeGap > 60 * 60) {// 1小时-24小时
            timeStr = timeGap / (60 * 60) + "小时前";
        } else if (timeGap > 60) {// 1分钟-59分钟
            timeStr = timeGap / 60 + "分钟前";
        } else {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        return timeStr;
    }

    /*
     * 是否过期
     */
    public static String restTime(String timestamp) {
        if (timestamp == null || "".equals(timestamp.trim()))
            return "";
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;

        diff = Long.parseLong(timestamp) * 1000;

        day = diff / nd;// 计算差多少天
        hour = diff % nd / nh + day * 24;// 计算差多少小时
        min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
        sec = diff % nd % nh % nm / ns;// 计算差多少秒

        return day + "天" + hour + "时" + min + "分" + sec + "秒";
    }


    /*
 * 时间distance
 */
    public static String getTimeDistance(long time) {
        long nd = 24 * 60 * 60;// 一天的秒数
        long nh = 60 * 60;// 一小时的秒数
        long nm = 60;// 一分钟的秒数

        long day = 0;
        long hour = 0;
        long min = 0;


        day = time / nd;// 计算差多少天
        hour = (time - day * nd) / 60 / 60;// 计算差多少小时
        min = (time - day * nd - hour * nh) / 60;// 计算差多少分钟

        if (day < 0) day = 0;
        if (hour < 0) hour = 0;
        if (min < 0) min = 0;
        return day + "天" + hour + "时" + min + "分";
    }

    //1000.00 >>>>>1,000.00
    public static String NumberFormat1(double num) {
        NumberFormat number_format = NumberFormat.getInstance(Locale.CHINA);
        return number_format.format(num);
    }


    //获得当天0点时间
    public static long getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() / 1000) * 1000;
    }


    /**
     * 将字符串转为时间戳
     *
     * @param dateString
     * @param pattern
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static boolean isNumeric00(String str) {
        try {
            Double.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //获得当天24点时间
    public static long getTimesnight(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis()/1000)*1000;
    }


    //保留两位小数
    public static double doubleValue(double num)
    {
        BigDecimal bigDecimal=new BigDecimal(num);
        return bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    //保留两位小数
    public static String doubleValue1(double num1)
    {
        String num=String.valueOf(doubleValue(num1));
        return num;
    }

}
