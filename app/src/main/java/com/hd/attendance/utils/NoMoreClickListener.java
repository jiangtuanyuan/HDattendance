package com.hd.attendance.utils;

import android.view.View;

import java.util.Calendar;

/**
 * Created by 蒋 on 2018/8/20.
 * 防止多次点击的单击事件
 */
public abstract class NoMoreClickListener implements View.OnClickListener {
    private int MIN_CLICK_DELAY_TIME = 3000;//多少秒点击一次 默认3秒
    private long lastClickTime = 0;

    public NoMoreClickListener() {

    }

    /**
     * 设置多少秒之内
     *
     * @param time
     */
    public NoMoreClickListener(int time) {
        this.MIN_CLICK_DELAY_TIME = time;
    }

    @Override
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            OnMoreClick(view);
        } else {
            OnMoreErrorClick();
        }
    }

    /**
     * 在N秒之内的 ==1 次点击回调次方法
     *
     * @param view
     */
    public void OnMoreClick(View view) {

    }

    /**
     * 在N秒之内的 >= 2次点击回调次方法
     */
    public void OnMoreErrorClick() {

    }
}