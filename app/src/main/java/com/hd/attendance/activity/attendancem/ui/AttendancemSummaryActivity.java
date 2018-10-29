package com.hd.attendance.activity.attendancem.ui;

import android.os.Bundle;

import com.hd.attendance.R;
import com.hd.attendance.base.BaseActivity;
import butterknife.ButterKnife;

public class AttendancemSummaryActivity extends BaseActivity {
    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_attendancem_summary);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle("考勤数据汇总");
    }

    @Override
    protected void initData() {

    }
}
