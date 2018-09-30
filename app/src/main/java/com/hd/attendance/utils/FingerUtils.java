package com.hd.attendance.utils;

import android.graphics.Bitmap;

import com.zkteco.android.biometric.core.utils.ToolUtils;
import com.zkteco.android.biometric.module.fingerprintreader.FingerprintSensor;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 蒋 on 2018/9/28.
 * 指纹仪工具类
 */

public class FingerUtils {

    /**
     * 将指纹仪的图片数据保存到本地
     *
     * @param fingerprintSensor 指纹仪
     * @param fpImage           图片的字节数组
     * @param filename          要保存的图片名称
     */
    public static void saveFingerBitmap(FingerprintSensor fingerprintSensor, byte[] fpImage, String filename) {
        int width = fingerprintSensor.getImageWidth();
        int height = fingerprintSensor.getImageHeight();
        ToolUtils.outputHexString(fpImage);//处理字节
        Bitmap bitmap = ToolUtils.renderCroppedGreyScaleBitmap(fpImage, width, height);
        File file = new File(FilePath.FINGER_IMAGES, filename);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
