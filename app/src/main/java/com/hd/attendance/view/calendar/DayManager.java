package com.hd.attendance.view.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 日期的管理类
 * Created by xiaozhu on 2016/8/7.
 */
public class DayManager {
    /**
     * 记录当前的时间
     */
    public static String currentTime;

    /**
     * 当前的日期
     */
    private static int current = -1;
    /**
     * 储存当前的日期
     */
    private static int tempcurrent = -1;
    /**
     *
     */
    static String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};
    static String[] dayArray = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
            "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

    /**
     * 设置当前的时间
     *
     * @param currentTime
     */
    public static void setCurrentTime(String currentTime) {
        DayManager.currentTime = currentTime;
    }

    /**
     * 获取当前的时间
     *
     * @return
     */
    public static String getCurrentTime() {
        return currentTime;
    }

    static Set<Integer> NormalDays = new HashSet<>();//正常上班天数

    public static void addNomalDays(int i) {
        NormalDays.add(i);
    }

    public static void ClearNomalDays() {
        NormalDays.clear();
    }

    static Set<Integer> LeavedDays = new HashSet<>();//请假天数

    public static void addLeavedDays(int i) {
        LeavedDays.add(i);
    }

    public static void ClearLeavedDays() {
        LeavedDays.clear();
    }

    static Set<Integer> AbsenteeisnDays = new HashSet<>();//旷工天数

    public static void addAbsenteeisnDays(int i) {
        AbsenteeisnDays.add(i);
    }

    public static void ClearAbsenteeisnDays() {
        AbsenteeisnDays.clear();
    }

    static Set<Integer> OvertimeDays = new HashSet<>();//加班天数

    public static void addOvertimeDays(int i) {
        OvertimeDays.add(i);
    }

    public static void ClearOvertimeDays() {
        OvertimeDays.clear();
    }

    static Set<Integer> TripDays = new HashSet<>();//出差天数

    public static void addTripDays(int i) {
        TripDays.add(i);
    }

    public static void ClearTripDays() {
        TripDays.clear();
    }

    /**
     * 清空五个出勤
     */
    public static void ClearAll() {
        NormalDays.clear();
        LeavedDays.clear();
        AbsenteeisnDays.clear();
        OvertimeDays.clear();
        TripDays.clear();
    }


    public static void setTempcurrent(int tempcurrent) {
        DayManager.tempcurrent = tempcurrent;
    }

    public static int getTempcurrent() {
        return tempcurrent;
    }


    public static void setCurrent(int current) {
        DayManager.current = current;
    }

    public static int select = -1;

    public static void setSelect(int select) {
        DayManager.select = select;
    }

    /**
     * 根据日历对象创建日期集合
     *
     * @param calendar 日历
     * @param width    控件的宽度
     * @param heigh    控件的高度
     * @return 返回的天数的集合
     */
    public static List<Day> createDayByCalendar(Calendar calendar, int width, int heigh) {

        List<Day> days = new ArrayList<>();
        Day day;
        int dayWidth = width / 7;
        int dayHeight = heigh / (calendar.getActualMaximum(Calendar.WEEK_OF_MONTH) + 1);
        //添加星期标识，
        for (int i = 0; i < 7; i++) {
            day = new Day(dayWidth, dayHeight);
            //为星期设置位置，为第0行，
            day.location_x = i;
            day.location_y = 0;
            day.text = weeks[i];
            //设置日期颜色
            day.textClor = 0xFF699CF0;//灰色
            day.workState = 5;
            days.add(day);
        }

        int count = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstWeekCount = calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
        //生成每一天的对象，其中第i次创建的是第i+1天

        for (int i = 0; i < count; i++) {
            day = new Day(dayWidth, dayHeight);
            day.text = dayArray[i];

            calendar.set(Calendar.DAY_OF_MONTH, i + 1);
            //设置每个天数的位置
            day.location_y = calendar.get(Calendar.WEEK_OF_MONTH);
            day.location_x = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            //设置日期选择状态
            if (i == current - 1) {
                //当前日期
                day.backgroundStyle = 3;
                day.textClor = 0xFF4384ED;

            } else if (i == select - 1) {
                day.backgroundStyle = 2;
                day.textClor = 0xFFFAFBFE;
            } else {
                day.backgroundStyle = 0;
                day.textClor = 0xFF8696A5;
            }

            //设置数字颜色 当天出勤状态
            if (NormalDays.contains(i + 1)) {
                //正常上班
                day.workState = 0;
            } else if (LeavedDays.contains(i + 1)) {
                day.workState = 1;
            } else if (AbsenteeisnDays.contains(i + 1)) {
                day.workState = 2;
            } else if (OvertimeDays.contains(i + 1)) {
                day.workState = 3;
            } else if (TripDays.contains(i + 1)) {
                day.workState = 4;
            } else {
                day.workState = 5;
            }
            days.add(day);
        }
        return days;
    }
}
