package com.hd.attendance.activity.main;

import android.widget.TextView;

/**
 * Created by 蒋 on 2018/9/22.
 * 主界面工具类
 */

public class MainUtils {
    /**
     * 时间效验
     * @param Timestr
     * @param tvLetf
     */
    public static void isCharmTime(String Timestr, TextView tvLetf) {
        String[] tims = Timestr.split(":");
        if (tims.length == 3) {
            try {
                int h = Integer.parseInt(tims[0]);//时
                int m = Integer.parseInt(tims[1]);//分

                //上午
                if (h < 8) {
                    tvLetf.setText("第一批次员工上班时间\n(打卡在08:00之前)");
                    return;
                }
                if (h == 8 && m <= 15) {
                    tvLetf.setText("第二批次员工上班时间\n(打卡在08:15之前)");
                    return;
                }
                if (h == 8 && m <= 30) {
                    tvLetf.setText("第三批次员工上班时间\n(打卡在08:30之前)");
                    return;
                }
                if (h == 8 && m <= 45) {
                    tvLetf.setText("第四批次员工上班时间\n(打卡在08:45之前)");
                    return;
                }
                if (h == 8 && m > 45) {
                    tvLetf.setText("上午上班打卡时间已结束!\n如果继续打卡算迟到处理!");
                    return;
                }
                if (h > 8 && h < 12) {
                    tvLetf.setText("上午上班时间!\n禁止打卡!\n否则算早退处理!");
                    return;
                }

                //下午
                if (h == 12 && m <= 59) {
                    tvLetf.setText("上午下班时间,请打卡!");
                    return;
                }
                if (h == 13 && m <= 30) {
                    tvLetf.setText("下午上班时间(13:30之前)\n请打卡!");
                    return;
                }
                if (h == 13 && m > 30) {
                    tvLetf.setText("下午上班打卡时间已结束!\n如果继续打卡算迟到处理!");
                    return;
                }

                if (h > 13 && h < 18) {
                    tvLetf.setText("下午上班时间!\n禁止打卡!\n否则算早退处理!");
                    return;
                }
                if (h == 18) {
                    tvLetf.setText("下午下班时间,请打卡~");
                    return;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
}
