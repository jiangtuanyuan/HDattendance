package com.hd.attendance.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.employees.ui.EmployeesActivity;
import com.hd.attendance.activity.fingerprint.ui.FingerMainActivity;
import com.hd.attendance.activity.group.ui.GroupMainActivity;
import com.hd.attendance.activity.health.ui.HealthMainActivity;
import com.hd.attendance.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 管理的界面
 */
public class ManagementActivity extends BaseActivity {

    @BindView(R.id.tv_kq)
    TextView tvKq;
    @BindView(R.id.tv_jc)
    TextView tvJc;
    @BindView(R.id.tv_yg)
    TextView tvYg;
    @BindView(R.id.tv_xz)
    TextView tvXz;
    @BindView(R.id.tv_ws)
    TextView tvWs;
    @BindView(R.id.tv_zw)
    TextView tvZw;

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_management);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle("系统管理");
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.tv_kq, R.id.tv_jc, R.id.tv_yg, R.id.tv_xz, R.id.tv_ws, R.id.tv_zw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_kq://考勤管理
                break;
            case R.id.tv_jc://就餐管理
                break;
            case R.id.tv_yg://员工管理
                startActivity(new Intent(this, EmployeesActivity.class));
                break;
            case R.id.tv_xz://小组管理
                startActivity(new Intent(this, GroupMainActivity.class));
                break;
            case R.id.tv_ws://卫生管理
                startActivity(new Intent(this, HealthMainActivity.class));
                break;
            case R.id.tv_zw://指纹管理
                startActivity(new Intent(this, FingerMainActivity.class));
                break;
            default:
                break;
        }
    }
}
