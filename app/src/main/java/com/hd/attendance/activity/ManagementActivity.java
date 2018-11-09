package com.hd.attendance.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.attendancem.ui.AttendancemMainActivity;
import com.hd.attendance.activity.employees.ui.EmployeesActivity;
import com.hd.attendance.activity.fingerprint.ui.FingerMainActivity;
import com.hd.attendance.activity.group.ui.GroupAddActivity;
import com.hd.attendance.activity.group.ui.GroupMainActivity;
import com.hd.attendance.activity.health.ui.HealthMainActivity;
import com.hd.attendance.activity.logs.LogsActivity;
import com.hd.attendance.activity.main.HDMainActivity;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.AttendancemTable;
import com.hd.attendance.utils.ToastUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_management);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle("系统管理");
        setSupportActionBar(toolbar);
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_system_logs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.logs:
                startActivity(new Intent(this, LogsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick({R.id.tv_kq, R.id.tv_jc, R.id.tv_yg, R.id.tv_xz, R.id.tv_ws, R.id.tv_zw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_kq://考勤管理
                startActivity(new Intent(this, AttendancemMainActivity.class));
                break;
            case R.id.tv_jc://就餐管理
                ToastUtil.showToast("就餐管理!");
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
