package com.hd.attendance.activity.main;


import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.db.AttendancemTable;
import com.hd.attendance.db.HealthTable;
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
                    tvAttenAfternoonStart.setText("  下午上班卡: " + attendancem.getAfternoon_start_time() + "【正常】");
                    break;
                case 1://迟到
                    tvAttenAfternoonStart.setText("  下午上班卡: " + attendancem.getAfternoon_start_time() + "【迟到】");
                    break;
                case 2://早退打卡
                    tvAttenAfternoonStart.setText("  下午上班卡: " + attendancem.getAfternoon_start_time() + "【早退】");
                    break;
                case 3://补卡
                    tvAttenAfternoonStart.setText("  下午上班卡: " + attendancem.getAfternoon_start_time() + "【补卡】");
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


}
