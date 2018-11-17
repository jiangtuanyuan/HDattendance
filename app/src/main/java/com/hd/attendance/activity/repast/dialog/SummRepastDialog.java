package com.hd.attendance.activity.repast.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.db.RepastTable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by 蒋 on 2018/11/3.
 * 汇总弹出的Fragment
 */

public class SummRepastDialog extends DialogFragment {
    @BindView(R.id.tv_month_infos)
    TextView tvMonthInfos;
    @BindView(R.id.tv_afternoon_report_sum)
    TextView tvAfternoonReportSum;
    @BindView(R.id.tv_afternoon_eat_sum)
    TextView tvAfternoonEatSum;
    @BindView(R.id.tv_evening_report_sum)
    TextView tvEveningReportSum;
    @BindView(R.id.tv_evening_eat_sum)
    TextView tvEveningEatSum;

    private String monthinfo = "";//月份信息

    private int noon_meal = 0;//中午报餐人数
    private int noon_eat = 0;
    private int evening_meal = 0;
    private int evening_eat = 0;
    private List<RepastTable> RepastList = new ArrayList<>();

    public void setRepastList(List<RepastTable> repastList) {
        RepastList = repastList;
    }

    public void setMonthinfo(String monthinfo) {
        this.monthinfo = monthinfo;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.arepast_sum_dialog, null);
        ButterKnife.bind(this, view);
        init();
        builder.setView(view);
        return builder.create();
    }

    private void init() {
        noon_meal = 0;//中午报餐人数
        noon_eat = 0;
        evening_meal = 0;
        evening_eat = 0;

        tvMonthInfos.setText(monthinfo);
        for (RepastTable e : RepastList) {
            if (e.isAfternoon_Report()) {
                noon_meal++;
            }
            if (e.isAfternoon_Eat()) {
                noon_eat++;
            }
            if (e.isEvening_Report()) {
                evening_meal++;
            }
            if (e.isAfternoon_Eat()) {
                evening_eat++;
            }
        }
        tvAfternoonReportSum.setText(noon_meal + "次");
        tvAfternoonEatSum.setText(noon_eat + "次");
        tvEveningReportSum.setText(evening_meal + "次");
        tvEveningEatSum.setText(evening_eat + "次");
    }
}
