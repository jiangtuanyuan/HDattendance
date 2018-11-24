package com.hd.attendance.view.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 日期的类
 * Created by xiaozhu on 2016/8/7.
 */
public class Day {
    /**
     * 单个日期格子的宽
     */
    public int width;
    /**
     * 单个日期格子的高
     */
    public int height;
    /**
     * 日期的文本
     */
    public String text;
    /**
     * 文本字体的颜色
     */
    public int textClor;

    /**
     * 打卡下标 -1 未打卡 其他是数据下标
     */
    public int index = -1;
    /**
     * 日期背景的类型 0代表无任何背景，1代表正常打卡，2代表所选日期，3代表当前日期 4,代表即是当前日期，也被选中
     */
    public int backgroundStyle;
    /**
     * 字体的大小
     */
    public float textSize;
    /**
     * 背景的半径
     */
    public int backgroundR;
    /**
     * 出勤的类型:0-正常班  1-请假 2-旷工 3-加班 4-出差
     */
    public int workState;
    /**
     * 字体在第几行
     */
    public int location_x;
    /**
     * 字体在第几列
     */
    public int location_y;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * 创建日期对象
     *
     * @param width  每个日期格子的宽度
     * @param height 每个日期格子的高度
     */
    public Day(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * 画天数
     *
     * @param canvas  要画的画布
     * @param paint   画笔
     * @param context 画布的上下文对象
     */
    public void drawDays(Canvas canvas, Context context, Paint paint) {
        //取窄的边框为圆的半径
        backgroundR = width > height ? height : width;
        //画背景
        drawBackground(canvas, paint);
        //画数字
        drawTaxt(canvas, paint);
    }

    /**
     * 花数字
     *
     * @param canvas
     * @param paint
     */
    private void drawTaxt(Canvas canvas, Paint paint) {
        //根据圆的半径设置字体的大小
        textSize = backgroundR / 3;
        paint.setTextSize(textSize);

        paint.setStyle(Paint.Style.FILL);
        //计算文字的宽度
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        int w = rect.width();
        //计算画文字的位置
        float x = location_x * width + (width - w) / 2;
        float y = location_y * height + (height + textSize / 2) / 2;
        //0为不画，1为正常考勤，2为异常
        switch (workState) {
            case 0://正常上班 绿色
                paint.setColor(Color.parseColor("#339933"));
                break;
            case 1://请假 黄色
                paint.setColor(Color.parseColor("#FF9224"));
                break;
            case 2://旷工 红色
                paint.setColor(Color.parseColor("#f60606"));
                break;
            case 3://加班 蓝色
                paint.setColor(Color.parseColor("#1DA1F2"));
                break;
            case 4://出差 紫色
                paint.setColor(Color.parseColor("#8B008B"));
                break;
            default:
                paint.setColor(Color.parseColor("#868789"));
                break;

        }
        if (backgroundStyle == 3) {
            paint.setColor(Color.parseColor("#ffffff"));
        }
        canvas.drawText(text, x, y, paint);

    }

    /**
     * 画背景
     *
     * @param canvas
     * @param paint
     */
    private void drawBackground(Canvas canvas, Paint paint) {
        //画背景 根据背景状态设置画笔类型
        if (backgroundStyle == 0) {
            return;
        }
        switch (backgroundStyle) {
            case 1:
                paint.setColor(0xFFECF1F4);
                paint.setStyle(Paint.Style.FILL);
                break;
            case 2:
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.parseColor("#39CBA4"));
                break;
            case 3:
                if (workState == 2) {
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.parseColor("#EC5D57"));
                    paint.setStrokeWidth(3);
                } else {
                    paint.setColor(Color.parseColor("#39CBA4"));
                    paint.setStyle(Paint.Style.FILL);
                    paint.setStrokeWidth(3);
                }
                break;
        }

        //计算圆心的位置
        float cx = location_x * width + width / 2;
        float cy = location_y * height + height / 2;
        canvas.drawCircle(cx, cy, backgroundR * 9 / 20, paint);
    }


}
