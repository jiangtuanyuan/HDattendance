package com.hd.attendance.activity.employees.ui;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.EmployeesTable;
import com.hd.attendance.utils.SystemLog;
import com.hd.attendance.utils.ToastUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新增
 */
public class EmployeesAddActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.rb_sex_male)
    RadioButton rbSexMale;
    @BindView(R.id.rb_sex_famale)
    RadioButton rbSexFamale;
    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    @BindView(R.id.et_jobs)
    EditText etJobs;
    @BindView(R.id.bt_save)
    Button btSave;

    //编辑的数据
    private int id;
    private String name = "", jobs = "", sex = "";
    private boolean isEditor = false;

    @Override
    protected void initVariables() {

        if (getIntent() != null) {
            id = getIntent().getIntExtra("id", 0);
            name = getIntent().getStringExtra("name");
            sex = getIntent().getStringExtra("sex");
            jobs = getIntent().getStringExtra("jobs");
        }
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_employees_add);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle("新增员工");
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(jobs) && !TextUtils.isEmpty(sex)) {
            isEditor = true;
            tvNum.setVisibility(View.VISIBLE);
            tvNum.setText("工     号: " + id);
            etName.setText(name + "");
            etName.setEnabled(false);
            etJobs.setText(jobs);
            if (sex.equals("男")) {
                rbSexMale.setChecked(true);
            } else {
                rbSexFamale.setChecked(true);
            }
            btSave.setText("修  改");
        }

    }


    @OnClick(R.id.bt_save)
    public void onViewClicked() {
        String name = etName.getText().toString()
                .replace(";", "")
                .replace("_", "")
                .replace(" ", "");
        String jobs = etJobs.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToast("姓名必填!");
            return;
        }
        if (TextUtils.isEmpty(jobs)) {
            ToastUtil.showToast("岗位必填!");
            return;
        }


        EmployeesTable e = new EmployeesTable();
        e.setName(name);
        if (rbSexFamale.isChecked()) {
            e.setSex("女");
        } else {
            e.setSex("男");
        }
        e.setJobs(etJobs.getText().toString());

        if (!isEditor) {
            e.save();
            SystemLog.getInstance().AddLog("管理员-新增员工:" + e.getName());

            ToastUtil.showToast("新增成功!");

            etName.setText("");
            etJobs.setText("");
        } else {
            e.update(id);

            SystemLog.getInstance().AddLog("管理员-修改了员工" + e.getName() + "的信息");
            ToastUtil.showToast("修改成功!");

        }
        finish();
    }
}
