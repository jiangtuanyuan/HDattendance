package com.hd.attendance.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangrongfeng on 2018/4/23.
 * 活动管理器
 */

public class ActivityCollector {

    public static List<Activity> sActivities = new ArrayList<>();
    public static void addActivity(Activity activity) {
        sActivities.add(activity);
    }

    public static void removeActvity(Activity activity) {
        sActivities.remove(activity);
    }
    //退出所有Activity
    public static void finishAll() {
        for (Activity activity : sActivities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
