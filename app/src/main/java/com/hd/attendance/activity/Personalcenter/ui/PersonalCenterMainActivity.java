package com.hd.attendance.activity.Personalcenter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.attendancem.ui.AttendancemSummaryActivity;
import com.hd.attendance.activity.repast.ui.ShowUserRepastActivity;
import com.hd.attendance.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalCenterMainActivity extends BaseActivity {
    @BindView(R.id.tv_kq)
    TextView tvKq;
    @BindView(R.id.tv_jc)
    TextView tvJc;

    private String UserID = "";
    private String UserName = "";

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            UserID = getIntent().getStringExtra("id");
            UserName = getIntent().getStringExtra("name");
        }

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal_center_main);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle(UserName + "—的个人中心");

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.tv_kq, R.id.tv_jc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_kq://进入到考勤管理
                Intent PerIntent = new Intent(this, AttendancemSummaryActivity.class);
                PerIntent.putExtra("id", UserID);
                PerIntent.putExtra("name", UserName);
                startActivity(PerIntent);
                break;
            case R.id.tv_jc:
                //进入到就餐管理
                Intent RepastIntent = new Intent(this, ShowUserRepastActivity.class);
                RepastIntent.putExtra("id", UserID);
                RepastIntent.putExtra("name", UserName);
                startActivity(RepastIntent);


                break;
            default:
                break;
        }
    }
}
