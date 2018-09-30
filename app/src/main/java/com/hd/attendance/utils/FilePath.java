package com.hd.attendance.utils;

import android.os.Environment;

/**
 * Created by 蒋 on 2018/9/28.
 * 文件路径
 */

public class FilePath {
    public static  String  ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static  String  FILE_PATH = ROOT+"/android/data/com.hd.attendance"; //APP存放的根目录
    public static  String  FINGER_IMAGES = FILE_PATH+"/FingerImages";// 存放指纹图片的目录
}
