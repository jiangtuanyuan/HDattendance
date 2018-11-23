package com.hd.attendance.activity.attendancem.dialog;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
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
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by 蒋 on 2018/8/31.
 * 日历
 */

public class PieChartDialog extends DialogFragment {
    @BindView(R.id.pic_chart)
    PieChart picChart;
    @BindView(R.id.tv_month)
    TextView tvMonth;

    private int month;
    private float[] attens;
    private List<AttendancemTable> AttenList = new ArrayList<>();

    public void setAttenList(List<AttendancemTable> attenList, int Month) {
        AttenList.clear();
        AttenList.addAll(attenList);
        this.month = Month;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.atten_piechart_dialog_layout, null);
        ButterKnife.bind(this, view);
        tvMonth.setText(month + "月份考勤饼状图");
        init();
        builder.setView(view);
        return builder.create();
    }


    private void init() {
        attens = new float[]{0, 0, 0, 0, 0};//出勤情况：正常上班,请假，旷工，加班，出差

        for (AttendancemTable atten : AttenList) {
            //2.筛选出勤情况
            switch (atten.getMorningWorkType()) {
                case WorkType.NORMAL_CODE://正常上班
                    attens[0] += 0.5;
                    break;
                case WorkType.LEAVE_CODE://请假
                    attens[1] += 0.5;
                    break;
                case WorkType.ABSENTEEISM_CODE://旷工
                    attens[2] += 0.5;
                    break;
                case WorkType.OVERTIME_CODE://加班
                    attens[3] += 0.5;
                    break;
                case WorkType.TRIP_CODE://出差
                    attens[4] += 0.5;
                    break;
                default:
                    break;
            }

            switch (atten.getAfternoonWorkType()) {
                case WorkType.NORMAL_CODE://正常上班
                    attens[0] += 0.5;
                    break;
                case WorkType.LEAVE_CODE://请假
                    attens[1] += 0.5;
                    break;
                case WorkType.ABSENTEEISM_CODE://旷工
                    attens[2] += 0.5;
                    break;
                case WorkType.OVERTIME_CODE://加班
                    attens[3] += 0.5;
                    break;
                case WorkType.TRIP_CODE://出差
                    attens[4] += 0.5;
                    break;
                default:
                    break;
            }




        }

        float attens_sum = attens[0] + attens[1] + attens[2] + attens[3] + attens[4];

        List<PieEntry> strings = new ArrayList<>();
        strings.add(new PieEntry(attens[0] / attens_sum * 100, "正常上班[" + attens[0] + "天]"));
        strings.add(new PieEntry(attens[1] / attens_sum * 100, "请假[" + attens[1] + "天]"));
        strings.add(new PieEntry(attens[2] / attens_sum * 100, "旷工[" + attens[2] + "天]"));
        strings.add(new PieEntry(attens[3] / attens_sum * 100, "加班[" + attens[3] + "天]"));
        strings.add(new PieEntry(attens[4] / attens_sum * 100, "出差[" + attens[4] + "天]"));

        PieDataSet dataSet = new PieDataSet(strings, "  出勤记录饼状图");

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.blue));
        colors.add(getResources().getColor(R.color.yellb));
        colors.add(getResources().getColor(R.color.red));
        colors.add(getResources().getColor(R.color.green1));
        colors.add(getResources().getColor(R.color.gray));

        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(18f);

        picChart.setData(pieData);
        picChart.invalidate();

        Description description = new Description();
        description.setText("");
        picChart.setDescription(description);
        picChart.setHoleRadius(0f);
        picChart.setTransparentCircleRadius(0f);

    }

}
