package com.hd.attendance.utils;

import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.hd.attendance.db.SystemLogTable;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蒋 on 2018/10/6.
 * 系统日志 文件的形式 存在数据库的
 */

public class SystemLog {
    private SystemLogTable ThisLog;
    private static SystemLog Systemlog;

    public static SystemLog getInstance() {
        if (Systemlog == null) {
            Systemlog = new SystemLog();
        }
        return Systemlog;
    }


    private SystemLog() {
        //判断数据库中是否存在今天的这条记录
        List<SystemLogTable> logTables = new ArrayList<>();

        logTables.addAll(LitePal.where("date = ? ", DateUtils.getYMD()).find(SystemLogTable.class));
        if (logTables.size() == 0) {
            ThisLog = new SystemLogTable();
            ThisLog.setDate(DateUtils.getYMD());
            ThisLog.setInfo("系统日志:");
            ThisLog.save();
        } else {
            ThisLog = logTables.get(0);
        }
    }

    /**
     * 添加一条记录
     *
     * @return
     */
    public void AddLog(String log) {
        ThisLog.setInfo(ThisLog.getInfo() + "\n" + " [ " + DateUtils.getTimeHM() + " ] " + log);
        ThisLog.save();
    }

    /**
     * 添加一条记录 并且显示
     *
     * @return
     */
    public void AddLog(String log, TextView tv) {
        ThisLog.setInfo(ThisLog.getInfo() + "\n" + " [ " + DateUtils.getTimeHM() + " ] " + log);
        ThisLog.save();
        tv.setText(ThisLog.getInfo());
    }

    /**
     * 获得今天的日志信息
     *
     * @return
     */
    public String getThisLog() {
        if (!TextUtils.isEmpty(ThisLog.getInfo())) {
            return ThisLog.getInfo();
        } else {
            return "";
        }
    }

    /**
     * 获得指定日期的日志信息 y-m-d
     *
     * @return
     */
    public String getThisLog(String date) {
        List<SystemLogTable> logTables = new ArrayList<>();
        logTables.addAll(LitePal.where("date = ?", date).find(SystemLogTable.class));
        if (logTables.size() == 0) {
            return "";
        } else {
            return logTables.get(0).getInfo();
        }
    }

    /**
     * 获得指定的年的月份的日志信息 y-m-d
     *
     * @return
     */
    public List<SystemLogTable> getMonthLog(String year, String month) {
        List<SystemLogTable> logTables = new ArrayList<>();
        Log.e("getMonthLog", year + "-" + month);
        logTables.addAll(LitePal.findAll(SystemLogTable.class));

        List<SystemLogTable> logs = new ArrayList<>();
        for (SystemLogTable s : logTables) {
            try {
                String dbyear = s.getDate().substring(0, 4);
                String dbmonth = s.getDate().substring(5, 7);
                if (dbyear.equals(year) && dbmonth.equals(month)) {
                    logs.add(s);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return logs;
    }
}
