package com.hd.attendance.activity;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import com.hd.attendance.activity.repast.ui.RepastMainActivity;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.AttendancemTable;
import com.hd.attendance.utils.SPUtils;
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
            case R.id.system_pwd:
                showAndminPwd();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 弹出输入的密码框
     */
    private AlertDialog.Builder AndminBuilder = null;
    private AlertDialog adminDialog = null;
    private EditText et_adminPwd = null;
    private TextView tv_Title = null;

    private void showAndminPwd() {
        if (AndminBuilder == null) {
            AndminBuilder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.show_admin_pwd_dialog, null);
            tv_Title = view.findViewById(R.id.tv_title);
            tv_Title.setText("系 统 密 码");

            et_adminPwd = view.findViewById(R.id.et_pwd);
            et_adminPwd.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            et_adminPwd.setText(SPUtils.getInstance().getString(SPUtils.SYSTEM_PWD, "123456"));

            view.findViewById(R.id.tv_canle).setOnClickListener(v -> {
                if (adminDialog != null) {
                    adminDialog.dismiss();
                }
            });

            view.findViewById(R.id.tv_ok).setOnClickListener(v -> {
                String pwd = et_adminPwd.getText().toString();
                if (!TextUtils.isEmpty(pwd)) {
                    if (adminDialog != null) {
                        SPUtils.getInstance().putString(SPUtils.SYSTEM_PWD, pwd);
                        et_adminPwd.setText(pwd);
                        adminDialog.dismiss();

                        ToastUtil.showToast("密码已修改!");
                    }
                } else {
                    ToastUtil.showToast("密码不能为空!");
                }
            });
            AndminBuilder.setView(view);
            adminDialog = AndminBuilder.create();
        } else {
            et_adminPwd.setText(SPUtils.getInstance().getString(SPUtils.SYSTEM_PWD, "123456"));
        }
        adminDialog.show();
    }


    @OnClick({R.id.tv_kq, R.id.tv_jc, R.id.tv_yg, R.id.tv_xz, R.id.tv_ws, R.id.tv_zw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_kq://考勤管理
                startActivity(new Intent(this, AttendancemMainActivity.class));
                break;
            case R.id.tv_jc://就餐管理
                startActivity(new Intent(this, RepastMainActivity.class));
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
