package com.hd.attendance.activity.main;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.attendancem.WorkType;
import com.hd.attendance.db.AttendancemTable;
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
            table.setWorkType(WorkType.NORMAL_CODE);//默认正常上班
            table.save();//保存之后再返回
            return table;
        }
    }
}
