package com.hd.attendance.activity.attendancem.dialog;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.hd.attendance.R;
import com.hd.attendance.activity.attendancem.WorkType;
import com.hd.attendance.db.AttendancemTable;
import com.hd.attendance.utils.DateUtils;
import com.hd.attendance.view.calendar.CalendarView;
import com.hd.attendance.view.calendar.DayManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by 蒋 on 2018/8/31.
 * 日历
 */

public class CanlendarDialog extends DialogFragment implements CalendarView.OnSelectChangeListener {


    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.calendar)
    CalendarView Mcalendar;
    @BindView(R.id.tv_msg)
    TextView tvMsg;

    private Calendar cal;//日历对象
    private SimpleDateFormat formatter;//格式化工具
    private Date curDate;//日期
    private String today;

    private int year;//要展示的年月
    private int month;

    private List<AttendancemTable> AttenList = new ArrayList<>();

    public void setAttenList(List<AttendancemTable> attenList, int Year, int Month) {
        AttenList.clear();
        AttenList.addAll(attenList);
        this.year = Year;
        this.month = Month;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.atten_canlendar_dialog_layout, null);
        ButterKnife.bind(this, view);

        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);
        Mcalendar.setOnSelectChangeListener(this);
        today = DateUtils.getToday(System.currentTimeMillis());
        init();
        builder.setView(view);
        return builder.create();
    }

    @SuppressLint("SimpleDateFormat")
    private void init() {
        Mcalendar.setCalendar(cal);

        formatter = new SimpleDateFormat("yyyy年MM月");
        //获取当前时间
        curDate = cal.getTime();
        String str = formatter.format(curDate);
        tvMonth.setText(str);

        DayManager.removeNomalDays();
        DayManager.removeAbnormalDays();

        for (AttendancemTable a : AttenList) {
            //正常上班
            if (a.getMorningWorkType() == WorkType.NORMAL_CODE && a.getAfternoonWorkType() == WorkType.NORMAL_CODE) {
                String day = a.getDate().substring(a.getDate().length() - 2, a.getDate().length());
                DayManager.addNomalDays(Integer.parseInt(day));
            }
        }

    }

    @Override
    public void selectChange(CalendarView calendarView, int date) {
        String strs = tvMonth.getText().toString()
                .replace("年", "-")
                .replace("月", "-")
                .replace(" ", "");
        if (date > 9) {
            strs += date;
        } else {
            strs += "0" + date;
        }
        strs.replace("年", "-");
        //显示相关信息
        for (AttendancemTable atten : AttenList) {
            if (atten.getDate().equals(strs)) {
                //1.设置头部信息 姓名  出勤情况:正常上班 打卡情况:
                StringBuffer sTop = new StringBuffer();
                sTop.append("出勤情况:");
                sTop.append("上午:");
                switch (atten.getMorningWorkType()) {
                    case 0:
                        sTop.append(" 正常上班 ");
                        break;
                    case 1:
                        sTop.append(" 请 假 ");
                        break;
                    case 2:
                        sTop.append(" 旷 工 ");
                        break;
                    case 3:
                        sTop.append(" 加 班 ");
                        break;
                    case 4:
                        sTop.append(" 出 差 ");
                        break;
                    default:
                        break;
                }

                sTop.append("  下午:");
                switch (atten.getAfternoonWorkType()) {
                    case 0:
                        sTop.append(" 正常上班 ");
                        break;
                    case 1:
                        sTop.append(" 请 假 ");
                        break;
                    case 2:
                        sTop.append(" 旷 工 ");
                        break;
                    case 3:
                        sTop.append(" 加 班 ");
                        break;
                    case 4:
                        sTop.append(" 出 差 ");
                        break;
                    default:
                        break;
                }
                tvMsg.setText(sTop.toString());
                break;
            } else {
                tvMsg.setText("无记录!");
            }
        }
    }

}
