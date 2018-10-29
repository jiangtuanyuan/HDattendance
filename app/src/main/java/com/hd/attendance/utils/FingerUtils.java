package com.hd.attendance.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.hd.attendance.db.FingerInfoTable;
import com.zkteco.android.biometric.core.utils.ToolUtils;
import com.zkteco.android.biometric.module.fingerprintreader.FingerprintSensor;

import org.litepal.LitePal;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * @param string
     * @return
     */
    public static Bitmap stringToBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 根据用户ID获得指纹数据
     */
    public static FingerInfoTable getFingerinfo(String UserID) {
        List<FingerInfoTable> infoTables = new ArrayList<>();
        infoTables.addAll(LitePal.where("User_ID = ?", UserID).find(FingerInfoTable.class));
        if (infoTables.size() > 0) {
            return infoTables.get(0);
        }
        return null;
    }
}
