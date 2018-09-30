package com.hd.attendance.utils;

/**
 * Created by 蒋 on 2018/9/22.
 * 时间管理器
 */

public class DateManagementUtil {
    /**
     * 正常上班时间
     */
    public static String[] DATE = new String[]{"1", "2", "3", "4", "5", "6"};

    /**
     * 一周放假
     */
    public static String[] DATE_TAKE = new String[]{"7"};

    /**
     * 上午上班时间 4个批次 之前打卡
     */
    public static final String MORNING_WORK_ONE = "08:00";
    public static final String MORNING_WORK_TWO = "08:15";
    public static final String MORNING_WORK_THREE = "08:30";
    public static final String MORNING_WORK_FOUR = "08:45";

    /**
     * 上午下班时间  之后打卡
     */
    public static final String MORNING_AFTER_WORK_DATE = "12:00";

    /**
     * 下午上班时间 之前打卡
     */
    public static final String AFTERNOON_WORK = "13:30";

    /**
     * 下午下班时间 之后打卡
     */
    public static final String AFTERNOON_AFTER_WORK = "18:00";

}
