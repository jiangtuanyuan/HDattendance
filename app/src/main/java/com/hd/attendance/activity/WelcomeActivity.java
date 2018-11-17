package com.hd.attendance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.hd.attendance.R;
import com.hd.attendance.activity.main.HDMainActivity;
import com.hd.attendance.base.BaseActivity;

import butterknife.ButterKnife;

public class WelcomeActivity extends BaseActivity {


    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        HiddenWindos();
        new Handler().postDelayed(() -> {
            startActivity(new Intent(WelcomeActivity.this, HDMainActivity.class));
            finish();
        }, 2500);
    }

    @Override
    protected void initData() {

    }
}
