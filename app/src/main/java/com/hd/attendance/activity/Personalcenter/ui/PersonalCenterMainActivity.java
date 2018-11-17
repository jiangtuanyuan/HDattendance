package com.hd.attendance.activity.Personalcenter.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.hd.attendance.R;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.utils.ZXingUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonalCenterMainActivity extends BaseActivity {

    @BindView(R.id.iv_zxing)
    ImageView ivZxing;

    private String UserID = "";
    private String UserNmae = "";

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            UserID = getIntent().getStringExtra("id");
            UserNmae = getIntent().getStringExtra("name");
        }

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal_center_main);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle("个人中心");


        Bitmap bitmap = ZXingUtils.createQRImage(UserID + ":" + UserNmae, 200, 200);
        ivZxing.setImageBitmap(bitmap);
    }

    @Override
    protected void initData() {

    }
}
