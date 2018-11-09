package com.hd.attendance.activity.attendancem.dialog;

import android.app.Dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.attendancem.AttendType;
import com.hd.attendance.activity.attendancem.WorkType;
import com.hd.attendance.db.AttendancemTable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by 蒋 on 2018/11/3.
 * 汇总弹出的Fragment
 */

public class SummaryAttenDialog extends DialogFragment {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_month_infos)
    TextView tvMonthInfos;
    @BindView(R.id.tv_work_number)
    TextView tvWorkNumber;
    @BindView(R.id.tv_leave_number)
    TextView tvLeaveNumber;
    @BindView(R.id.tv_overtime_number)
    TextView tvOvertimeNumber;
    @BindView(R.id.tv_absenteeism_number)
    TextView tvAbsenteeismNumber;
    @BindView(R.id.tv_trip_number)
    TextView tvTripNumber;
    @BindView(R.id.tv_morning_start_info)
    TextView tvMorningStartInfo;
    @BindView(R.id.tv_morning_end_info)
    TextView tvMorningEndInfo;
    @BindView(R.id.tv_afternoon_start_info)
    TextView tvAfternoonStartInfo;
    @BindView(R.id.tv_afternoon_end_info)
    TextView tvAfternoonEndInfo;
    @BindView(R.id.tv_deductions_number)
    TextView tvDeductionsNumber;
    @BindView(R.id.tv_deductions_money)
    TextView tvDeductionsMoney;

    private String name = "";//姓名

    public void setName(String name) {
        this.name = name;
    }

    private String monthinfo = "";//月份信息

    public void setMonthinfo(String monthinfo) {
        this.monthinfo = monthinfo;
    }

    //数据
    private List<AttendancemTable> AttenList = new ArrayList<>();

    public void setAttenList(List<AttendancemTable> attenList) {
        AttenList = attenList;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.atten_sum_dialog, null);
        ButterKnife.bind(this, view);
        init();
        builder.setView(view);
        return builder.create();
    }

    private float[] attens;
    private int[] morning_starts;
    private int[] morning_ends;
    private int[] afternoon_starts;
    private int[] afternoon_ends;
    private float[] deductions;

    private void init() {
        attens = new float[]{0, 0, 0, 0, 0};//出勤情况：正常上班,请假，旷工，加班，出差
        morning_starts = new int[]{0, 0, 0, 0};//上午上班：正常打卡，迟到打卡，早退打卡，补卡
        morning_ends = new int[]{0, 0, 0, 0};//上午下班：
        afternoon_starts = new int[]{0, 0, 0, 0};//下午上班：
        afternoon_ends = new int[]{0, 0, 0, 0};//下午下班：
        deductions = new float[]{0, 0};//扣款： 次数,金额
        //1.设置头部信息
        tvName.setText(name);
        tvMonthInfos.setText(monthinfo);

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
            //3.打卡情况
            //上午上班
            switch (atten.getMorning_start_type()) {
                case AttendType.NORMAL_CODE://正常打卡
                    morning_starts[0]++;
                    break;
                case AttendType.LATE_CODE://迟到打卡
                    morning_starts[1]++;
                    break;
                case AttendType.LEAVE_CODE://早退打卡
                    morning_starts[2]++;
                    break;
                case AttendType.FILL_CODE://补卡
                    morning_starts[3]++;
                    break;
                default:
                    break;
            }
            //上午下班
            switch (atten.getMorning_end_type()) {
                case AttendType.NORMAL_CODE://正常打卡
                    morning_ends[0]++;
                    break;
                case AttendType.LATE_CODE://迟到打卡
                    morning_ends[1]++;
                    break;
                case AttendType.LEAVE_CODE://早退打卡
                    morning_ends[2]++;
                    break;
                case AttendType.FILL_CODE://补卡
                    morning_ends[3]++;
                    break;
                default:
                    break;
            }

            //下午上班
            switch (atten.getAfternoon_start_type()) {
                case AttendType.NORMAL_CODE://正常打卡
                    afternoon_starts[0]++;
                    break;
                case AttendType.LATE_CODE://迟到打卡
                    afternoon_starts[1]++;
                    break;
                case AttendType.LEAVE_CODE://早退打卡
                    afternoon_starts[2]++;
                    break;
                case AttendType.FILL_CODE://补卡
                    afternoon_starts[3]++;
                    break;
                default:
                    break;
            }
            //上午下班
            switch (atten.getAfternoon_end_type()) {
                case AttendType.NORMAL_CODE://正常打卡
                    afternoon_ends[0]++;
                    break;
                case AttendType.LATE_CODE://迟到打卡
                    afternoon_ends[1]++;
                    break;
                case AttendType.LEAVE_CODE://早退打卡
                    afternoon_ends[2]++;
                    break;
                case AttendType.FILL_CODE://补卡
                    afternoon_ends[3]++;
                    break;
                default:
                    break;
            }

            if (atten.isDeductions()) {
                deductions[0]++;
                deductions[1] += atten.getDeductions();
            }
        }
        initToView();
    }

    private void initToView() {

        //1.出勤情况
        tvWorkNumber.setText(attens[0] + "天");
        tvLeaveNumber.setText(attens[1] + "天");
        tvAbsenteeismNumber.setText(attens[2] + "天");
        tvOvertimeNumber.setText(attens[3] + "天");
        tvTripNumber.setText(attens[4] + "天");

        //2.打卡情况
        tvMorningStartInfo.setText("正常打卡:" + morning_starts[0] + "次    迟到打卡:" + morning_starts[1] + "次    补卡:" + morning_starts[3] + "次");
        tvMorningEndInfo.setText("正常打卡:" + morning_ends[0] + "次    早退打卡:" + morning_ends[2] + "次    补卡:" + morning_starts[3] + "次");
        tvAfternoonStartInfo.setText("正常打卡:" + afternoon_starts[0] + "次    迟到打卡:" + afternoon_starts[1] + "次    补卡:" + afternoon_starts[3] + "次");
        tvAfternoonEndInfo.setText("正常打卡:" + afternoon_ends[0] + "次    早退打卡:" + afternoon_ends[2] + "次    补卡:" + afternoon_ends[3] + "次");

        //3.扣款情况
        tvDeductionsNumber.setText(deductions[0] + " 次");
        tvDeductionsMoney.setText(deductions[1] + " 元");

    }

}
