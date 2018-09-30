package com.hd.attendance.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 蒋 on 2018/9/22.
 * 时间工具类
 */

public class DateUtils {
    /**
     * 获取时间 yyyy-MM-dd hh:mm:ss
     */
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat datedf = new SimpleDateFormat("yyyy年MM月dd日");
    private static SimpleDateFormat Timedf = new SimpleDateFormat("HH:mm:ss");

    private static String dayNames[] = {"星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public static String getDate() {
        try {
            //如果hh为小写 那么就搜12小时制 如果为大写 那么就是24小时制
            return df.format(System.currentTimeMillis());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取Time
     * HH:mm:ss
     */
    public static String getTime() {
        try {
            //如果hh为小写 那么就搜12小时制 如果为大写 那么就是24小时制
            return Timedf.format(System.currentTimeMillis());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 通过日期判断是周几
     */
    public static String DateToDay(String daydate) {
        Calendar c = Calendar.getInstance();// 获得一个日历的实例
        try {
            c.setTime(df.parse(daydate));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        return dayNames[c.get(Calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * 返回当天是星期几  1 2 3 4 5 6 7
     */
    public static int getWeek() {
        Calendar c = Calendar.getInstance();// 获得一个日历的实例
        return c.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 获取当前 年月日和星期
     * 例如 2018年09月22日
     */
    public static String getYearMonthDayWeek() {
        try {
            //如果hh为小写 那么就搜12小时制 如果为大写 那么就是24小时制
            return datedf.format(System.currentTimeMillis()) + " (" + DateToDay(getDate()) + ")";
        } catch (Exception e) {
            return "";
        }
    }
}
