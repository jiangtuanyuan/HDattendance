package com.hd.attendance.activity.main;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.db.AttendancemTable;
import com.hd.attendance.db.EmployeesTable;
import com.hd.attendance.db.HealthTable;
import com.hd.attendance.db.RepastTable;
import com.hd.attendance.utils.DateUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蒋 on 2018/9/22.
 * 主界面工具类
 */

public class MainUtils {

    /**
     * 根据用户ID 和日期 拿到一条对应的考勤数据
     *
     * @param UserID
     * @param date   一定要 2018-10-09 的格式
     */
    public static AttendancemTable AddAttendancem(int UserID, String UserName, String date) {
        List<AttendancemTable> AList = new ArrayList<>();
        AList.addAll(LitePal.where("User_ID = ? and Date = ?", UserID + "", date).find(AttendancemTable.class));
        if (AList.size() > 0) {
            return AList.get(0);
        } else {
            AttendancemTable table = new AttendancemTable();
            table.setUser_ID(UserID);
            table.setUser_Name(UserName);
            table.setDate(date);
            table.setWeek(DateUtils.DateToDayB(date));
            table.save();//保存之后再返回
            return table;
        }
    }

    /**
     * 根据用户ID 拿到用户员工表的数据
     *
     * @param UserID
     */
    public static EmployeesTable getEmployeess(String UserID) {
        List<EmployeesTable> AList = new ArrayList<>();
        AList.addAll(LitePal.where("id = ?", UserID).find(EmployeesTable.class));
        if (AList.size() > 0) {
            return AList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据用户ID 和日期 拿到一条对应的就餐数据
     *
     * @param UserID
     * @param date   一定要 2018-10-09 的格式
     */
    public static RepastTable getUserRepastTable(int UserID, String UserName, String date) {
        List<RepastTable> AList = new ArrayList<>();
        AList.addAll(LitePal.where("User_ID = ? and Date = ?", UserID + "", date).find(RepastTable.class));
        if (AList.size() > 0) {
            return AList.get(0);
        } else {
            RepastTable table = new RepastTable();
            table.setUser_ID(UserID);
            table.setUser_Name(UserName);
            table.setDate(date);
            table.setWeek(DateUtils.DateToDayB(date));
            table.save();//保存之后再返回
            return table;
        }
    }


    /**
     * 显示头部日期周次的展示
     *
     * @param tvTopWeeks
     * @param tvDate
     * @param tvWeek
     */
    public static void showTopTime(TextView tvTopWeeks, TextView tvDate, TextView tvWeek) {
        //1.显示今天是周几
        int weekid = DateUtils.getWeek();
        if (weekid == 6) {
            tvTopWeeks.setText("【周六大扫除-" + (DateUtils.getThisWeeks() % 2 == 0 ? "双周" : "单周") + "】");
        } else {
            tvTopWeeks.setText("【" + DateUtils.DateToDayB(DateUtils.getYMD()) + "】");
        }
        //2.显示当前年月日
        tvDate.setText(DateUtils.getYMD());
        //3.显示今天是当年的第多少周
        tvWeek.setText("【第" + DateUtils.getThisWeeks() + "周】");
    }

    /**
     * 显示今日的卫生安排
     *
     * @param tvHealthAuser
     * @param tvHealthAuserInfo
     * @param tvHealthBuser
     * @param tvHealthBuserInfo
     */
    public static HealthTable showHealth(TextView tvHealthAuser, TextView tvHealthAuserInfo, TextView tvHealthBuser, TextView tvHealthBuserInfo) {
        List<HealthTable> HList = new ArrayList<>();

        int weekid = DateUtils.getWeek();
        if (weekid == 6) {
            //拿大扫除的卫生安排
            int number = DateUtils.getThisWeeks();
            if (number % 2 == 0) {
                HList.addAll(LitePal.where("weekid = 6 and weeks= 2").find(HealthTable.class));
            } else {
                HList.addAll(LitePal.where("weekid = 6 and weeks= 1").find(HealthTable.class));
            }
        } else {
            HList.addAll(LitePal.where("weekid = ?", String.valueOf(weekid)).find(HealthTable.class));
        }

        if (HList.size() > 0) {

            tvHealthAuser.setText(HList.get(0).getOnFloorUserName());
            tvHealthAuserInfo.setText(HList.get(0).getOnFloorInfo());
            tvHealthBuser.setText(HList.get(0).getTwoFloorUserName());
            tvHealthBuserInfo.setText(HList.get(0).getTwoFloorInfo());

            return HList.get(0);
        } else {
            tvHealthAuser.setText("无");
            tvHealthAuserInfo.setText("无");
            tvHealthBuser.setText("无");
            tvHealthBuserInfo.setText("无");
            return null;
        }
    }


    // ATTEN_CODE值的说明: 控 制
    // 0：上午上班正常时间 在7:00 之后 8:45之前   上午正常: ATTEN_CODE 为 0
    // 1: 上午上班迟到    8:45之后 09:00 之前    上午迟到：ATTEN_CODE 为 1
    // 2: 上午上班早退    09:00 之后 12:00之前   上午上班早退卡: ATTEN_CODE 2
    // 3：上午下班打卡时间 在12:00之后 13:00之前  ATTEN_CODE为 3

    // 4：下午上班正常时间  在13:00之后 13:30之前  ATTEN_CODE为 4
    // 5: 下午上班迟到    13:30之后 14:00 之前    下午午迟到：ATTEN_CODE 为 5
    // 6:  下午上班早退   14:00 之后 18：00 之前   下午上班早退 ATTEN_CODE 为 6

    // 7：下午下班时间    在18:00之后 23:59之前  ATTEN_CODE为7

    // 8:禁止任何打卡时间  在00:00 之后 7:00之前  ATTEN_CODE为8

    /**
     * 效验时间返回状态码
     *
     * @param time        时间
     * @param AttenPrompt 考勤打卡提示
     * @return
     */
    public static int isCharmTime(String time, TextView AttenPrompt, ImageView ivState) {
        String[] times = time.split(":");
        if (times.length == 3) {
            try {
                int h = Integer.parseInt(times[0]);//时
                int m = Integer.parseInt(times[1]);//分

                //跟随图标变换时间图标
                if (h > 0 && h < 12) {
                    ivState.setImageResource(R.drawable.ic_morning);
                } else {
                    ivState.setImageResource(R.drawable.ic_afternoon);
                }

                if (h > 0 && h < 7) {
                    //打卡在7点之后开始 这段时间禁止打卡的
                    AttenPrompt.setText("【00:00-7:00】-禁止考勤指纹操作！");

                    return 8;
                }

                //上午上班打卡  为0
                if (h < 8) {
                    AttenPrompt.setText("【07:00-08:00】第一批次员工上班时间!请打卡!");
                    return 0;
                }

                if (h == 8 && m <= 15) {
                    AttenPrompt.setText("【08:00-08:15】第二批次员工上班时间!请打卡!");
                    return 0;
                }

                if (h == 8 && m <= 30) {
                    AttenPrompt.setText("【08:15-08:30】第三批次员工上班时间!请打卡!");
                    return 0;
                }

                if (h == 8 && m <= 45) {
                    AttenPrompt.setText("【08:30-08:45】第四批次员工上班时间!请打卡!");
                    return 0;
                }

                //【上午上班】-迟到
                if (h == 8 && m > 45) {
                    AttenPrompt.setText("【上午上班】-打卡时间已结束! 打卡算【迟到】状态!");
                    return 1;
                }

                //【上午上班】-早退
                if (h > 8 && h < 12) {
                    AttenPrompt.setText("【上午上班中】(08:45-12:00)-禁止打卡,否则算【早退】处理!");
                    return 2;
                }


                //上午下班时间
                if (h == 12 && m <= 59) {
                    AttenPrompt.setText("【上午下班】(12:00-13:00) 请打卡!");
                    return 3;
                }

                //下午上班时间
                if (h == 13 && m <= 30) {
                    AttenPrompt.setText("【下午上班】(13:00-13:30) 请打卡!");

                    return 4;
                }
                //下午上班时间迟到
                if (h == 13 && m > 30) {
                    AttenPrompt.setText("【下午上班】打卡时间已结束! 打卡算【迟到】处理!");
                    return 5;
                }

                //下午下班早退
                if (h > 13 && h < 18) {
                    AttenPrompt.setText("【下午上班】(13:30-18:00) 打卡算【早退】处理!");
                    return 6;
                }
                //下午下班
                if (h >= 18) {
                    AttenPrompt.setText("【下午下班】(18:00-23:59),请打卡~");
                    return 7;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return 4;
            }
        }
        return 4;
    }

    /**
     * 就餐打卡时间Code
     *
     * @return
     */
    public static int isRepastTime(String time, TextView tvCenterRepastPrompt) {
        try {
            String[] times = time.split(":");
            if (times.length == 3) {
                int h = Integer.parseInt(times[0]);//时
                int m = Integer.parseInt(times[1]);//分
                if (h > 0 && h < 7) {
                    //打卡在7点之后开始 这段时间禁止打卡的
                    tvCenterRepastPrompt.setText("【00:00-7:00】-禁止就餐指纹仪操作！");
                    return 0;
                }
                if (h <= 9 && m <= 30) {
                    //中午报餐时间   9：30之前报餐
                    tvCenterRepastPrompt.setText("【午餐报餐】07:00-09:30 之间 请轻触右边指纹仪报中餐！");
                    return 1;
                }

                if (h == 12 && m <= 59) {
                    //中午就餐打卡时间    12:00-13：00
                    tvCenterRepastPrompt.setText("【午餐就餐】12:00-13:00 之间 请轻触右边指纹仪确定就餐！");
                    return 2;
                }

                if (h <= 14 && m <= 20) {
                    //晚餐报餐时间   14：20之前报餐
                    tvCenterRepastPrompt.setText("【晚餐报餐】13:00-14:20 之间 请轻触右边指纹仪报晚餐！");
                    return 3;
                }

                if (h >= 18) {
                    //晚餐就餐打卡时间    18：00以后
                    tvCenterRepastPrompt.setText("【晚餐就餐】 18：00之后 请轻触右边指纹仪确定就餐！");
                    return 4;
                }
            }
            tvCenterRepastPrompt.setText("未到报餐或者就餐打卡时间!");
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 分离就餐时间和考勤时间
     */
    public static int isAttenOrRepast(String time) {
        try {
            String[] times = time.split(":");
            if (times.length == 3) {
                int h = Integer.parseInt(times[0]);//时
                if (h >= 7 && h < 12) {
                    //考勤模式
                    return 0;
                }

                if (h == 12) {
                    //就餐模式
                    return 1;
                }
                if (h > 12 && h < 18) {
                    //考勤模式
                    return 0;
                }
                if (h == 18) {
                    //就餐模式
                    return 1;
                }
                if (h > 18) {
                    //考勤模式
                    return 0;
                }
            }
            return 0;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 显示今日打卡记录
     */
    public static void showAttendance(AttendancemTable attendancem, TextView name, TextView tvAttenMorningStart, TextView tvAttenMorningEnd, TextView tvAttenAfternoonStart, TextView tvAttenAfternoonEnd) {
        name.setText("姓名:" + attendancem.getUser_Name());

        if (!TextUtils.isEmpty(attendancem.getMorning_start_time())) {
            switch (attendancem.getMorning_start_type()) {
                case 0://正常
                    tvAttenMorningStart.setText("上午上班卡:" + attendancem.getMorning_start_time() + "【正常】");
                    break;
                case 1://迟到
                    tvAttenMorningStart.setText("上午上班卡:" + attendancem.getMorning_start_time() + "【迟到】");
                    break;
                case 2://早退打卡
                    tvAttenMorningStart.setText("上午上班卡:" + attendancem.getMorning_start_time() + "【早退】");
                    break;
                case 3://补卡
                    tvAttenMorningStart.setText("上午上班卡:" + attendancem.getMorning_start_time() + "【补卡】");
                    break;
                default:
                    break;
            }
        } else {
            tvAttenMorningStart.setText("上午上班卡: 暂未打卡");
        }


        //上午下班
        if (!TextUtils.isEmpty(attendancem.getMorning_end_time())) {
            switch (attendancem.getMorning_end_type()) {
                case 0://正常
                    tvAttenMorningEnd.setText("上午下班卡: " + attendancem.getMorning_end_time() + "【正常】");
                    break;
                case 1://迟到
                    tvAttenMorningEnd.setText("上午下班卡: " + attendancem.getMorning_end_time() + "【迟到】");
                    break;
                case 2://早退打卡
                    tvAttenMorningEnd.setText("上午下班卡: " + attendancem.getMorning_end_time() + "【早退】");
                    break;
                case 3://补卡
                    tvAttenMorningEnd.setText("上午下班卡: " + attendancem.getMorning_end_time() + "【补卡】");
                    break;
                default:
                    break;
            }
        } else {
            tvAttenMorningEnd.setText("上午下班卡: 暂未打卡");
        }

        //下午上班卡
        if (!TextUtils.isEmpty(attendancem.getAfternoon_start_time())) {
            switch (attendancem.getAfternoon_start_type()) {
                case 0://正常
                    tvAttenAfternoonStart.setText("下午上班卡: " + attendancem.getAfternoon_start_time() + "【正常】");
                    break;
                case 1://迟到
                    tvAttenAfternoonStart.setText("下午上班卡: " + attendancem.getAfternoon_start_time() + "【迟到】");
                    break;
                case 2://早退打卡
                    tvAttenAfternoonStart.setText("下午上班卡: " + attendancem.getAfternoon_start_time() + "【早退】");
                    break;
                case 3://补卡
                    tvAttenAfternoonStart.setText("下午上班卡: " + attendancem.getAfternoon_start_time() + "【补卡】");
                    break;
                default:
                    break;
            }
        } else {
            tvAttenAfternoonStart.setText("上午上班卡: 暂未打卡");
        }


        //下午下班
        if (!TextUtils.isEmpty(attendancem.getAfternoon_end_time())) {
            switch (attendancem.getAfternoon_end_type()) {
                case 0://正常
                    tvAttenAfternoonEnd.setText("下午下班卡: " + attendancem.getAfternoon_end_time() + "【正常】");
                    break;
                case 1://迟到
                    tvAttenAfternoonEnd.setText("下午下班卡: " + attendancem.getAfternoon_end_time() + "【迟到】");
                    break;
                case 2://早退打卡
                    tvAttenAfternoonEnd.setText("下午下班卡: " + attendancem.getAfternoon_end_time() + "【早退】");
                    break;
                case 3://补卡
                    tvAttenAfternoonEnd.setText("下午下班卡: " + attendancem.getAfternoon_end_time() + "【补卡】");
                    break;
                default:
                    break;
            }
        } else {
            tvAttenAfternoonEnd.setText("下午下班卡: 暂未打卡");
        }

    }

    /**
     * 显示今日报餐 记录
     *
     * @param repastTable
     * @param tvRightLogName
     * @param tvRightLogAfternoonMeal
     * @param tvRightLogAfternoonEatMeal
     * @param tvRightLogEveningMeal
     * @param tvRightLogEveningEatMeal
     */
    public static void showRepast(RepastTable repastTable, TextView tvRightLogName,
                                  TextView tvRightLogAfternoonMeal,
                                  TextView tvRightLogAfternoonEatMeal,
                                  TextView tvRightLogEveningMeal,
                                  TextView tvRightLogEveningEatMeal) {
        tvRightLogName.setText("姓名：" + repastTable.getUser_Name());

        if (repastTable.isAfternoon_Report()) {
            tvRightLogAfternoonMeal.setText("中餐是否报餐:是 【" + repastTable.getAfternoon_Report_time() + "】");
        } else {
            tvRightLogAfternoonMeal.setText("中餐是否报餐:否");
        }

        if (repastTable.isAfternoon_Eat()) {
            tvRightLogAfternoonEatMeal.setText("中餐是否就餐:是 【" + repastTable.getAfternoon_Eat_time() + "】");
        } else {
            tvRightLogAfternoonEatMeal.setText("中餐是否就餐:否");
        }

        //晚餐
        if (repastTable.isEvening_Report()) {
            tvRightLogEveningMeal.setText("晚餐是否报餐:是 【" + repastTable.getEvening_Report_time() + "】");
        } else {
            tvRightLogEveningMeal.setText("晚餐是否报餐:否");
        }

        if (repastTable.isEvening_Eat()) {
            tvRightLogEveningEatMeal.setText("晚餐是否就餐:是 【" + repastTable.getEvening_Eat_time() + "】");
        } else {
            tvRightLogEveningEatMeal.setText("晚餐是否就餐:否");
        }

    }


    //获取版本号
    public static String getVersion(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "未知版本";
        }
    }

    /**
     * 查找员工中的管理员
     */
    public static List<EmployeesTable> getAdminEmp() {
        List<EmployeesTable> admins = new ArrayList<>();
        admins.addAll(LitePal.where("administrator = ?", "1").find(EmployeesTable.class));
        return admins;
    }

}
