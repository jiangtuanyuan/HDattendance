package com.hd.attendance.activity.fingerprint.ui;

import android.os.Bundle;

import com.hd.attendance.R;
import com.hd.attendance.base.BaseActivity;

/**
 * 添加指纹
 */
public class FingerAddActivity extends BaseActivity {
    @Override
    protected void initVariables() {

    }
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_finger_add);
        initToolbarNav();
        setTitle("添加指纹");
    }

    @Override
    protected void initData() {
    }
}
