package com.hd.attendance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.main.HDMainActivity;
import com.hd.attendance.activity.main.MainUtils;
import com.hd.attendance.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends BaseActivity {

    //版本
    @BindView(R.id.tv_version)
    TextView TvVersion;//版本
    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        HiddenWindos();
        TvVersion.setText("版本:" + MainUtils.getVersion(this));
        new Handler().postDelayed(() -> {
            startActivity(new Intent(WelcomeActivity.this, HDMainActivity.class));
            finish();
        }, 2500);
    }

    @Override
    protected void initData() {

    }
}
