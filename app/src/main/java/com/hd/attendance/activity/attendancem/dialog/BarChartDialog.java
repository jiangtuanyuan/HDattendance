package com.hd.attendance.activity.attendancem.dialog;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.hd.attendance.R;
import com.hd.attendance.activity.attendancem.WorkType;
import com.hd.attendance.db.AttendancemTable;
import com.hd.attendance.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by 蒋 on 2018/8/31.
 * 柱状图
 */

public class BarChartDialog extends DialogFragment {
    @BindView(R.id.pic_chart)
    BarChart mBarChart;
    @BindView(R.id.tv_month)
    TextView tvMonth;

    private int year;
    private int month;
    private int monthDay;//当前月份有多少天
    private float[] attens;
    private List<AttendancemTable> AttenList = new ArrayList<>();

    public void setAttenList(List<AttendancemTable> attenList, int Year, int Month) {
        AttenList.clear();
        AttenList.addAll(attenList);
        this.month = Month;
        this.year = Year;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.atten_barchart_dialog_layout, null);
        ButterKnife.bind(this, view);
        tvMonth.setText(year + "年" + month + "月份考勤柱状图");
        //得到当前年当前月多少天
        monthDay = DateUtils.getMonthLastDay(year, month);
        initData();

        initBarChart();
        builder.setView(view);
        return builder.create();
    }


    private void initData() {
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
    }

    /**
     * 初始化BarChart图表
     */
    private void initBarChart() {
        Description description = new Description();
        description.setText("-月度出勤柱状图-");
        description.setEnabled(false);
        mBarChart.setDescription(description);

        mBarChart.setTouchEnabled(false);//禁止所有点击拖拽事件

        mBarChart.setMaxVisibleValueCount(60);
        mBarChart.setPinchZoom(false);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawGridBackground(false);

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(0f);//设置最小间隔，防止当放大时，出现重复标签。


        mBarChart.getAxisLeft().setDrawGridLines(true);
        mBarChart.getAxisLeft().setAxisMaximum(monthDay);

        mBarChart.getAxisRight().setEnabled(false);
        mBarChart.getLegend().setEnabled(false);
        setDataToBar();
    }

    /**
     * 设置数据刀柱状图
     */
    private void setDataToBar() {
        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        yVals1.add(new BarEntry(1f, attens[0]));
        yVals1.add(new BarEntry(2f, attens[1]));
        yVals1.add(new BarEntry(3f, attens[2]));
        yVals1.add(new BarEntry(4f, attens[3]));
        yVals1.add(new BarEntry(5f, attens[4]));

        BarDataSet barDataSet = new BarDataSet(yVals1, "");
        barDataSet.setDrawValues(true);
        barDataSet.setValueTextSize(10f);

        List<Integer> Colors = new ArrayList<>();
        Colors.add(Color.parseColor("#339933"));
        Colors.add(Color.parseColor("#FF9224"));
        Colors.add(Color.parseColor("#f60606"));
        Colors.add(Color.parseColor("#1DA1F2"));
        Colors.add(Color.parseColor("#8B008B"));

        barDataSet.setColors(Colors);

        BarData barData = new BarData(barDataSet);
        mBarChart.setData(barData);
        mBarChart.setFitBars(true);
        mBarChart.animateY(1500);
    }


}
