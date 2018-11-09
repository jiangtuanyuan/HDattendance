package com.hd.attendance.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by 蒋 on 2018/11/3.
 * 就餐表
 */

public class RepastTable extends LitePalSupport {
    @Column
    private int id; //不可构造set方法 自增ID
    @Column
    private int User_ID;//员工ID
    @Column
    private String User_Name;//员工姓名

    @Column
    private String Date;//当天的日期 2018-10-20
    @Column
    private String Week;//当天星期几 周六

    @Column
    private boolean Afternoon_Report;// 中餐是否报餐 false and true
    @Column
    private boolean Afternoon_Eat;// 中餐是否吃了饭 false and true

    @Column
    private boolean Evening_Report;// 晚餐是否报餐 false and true
    @Column
    private boolean Evening_Eat;// 晚餐是否吃了饭 false and true

    @Column
    private String Note;//备注内容

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        this.Note = note;
    }

    public int getId() {
        return id;
    }

    public int getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getWeek() {
        return Week;
    }

    public void setWeek(String week) {
        Week = week;
    }

    public boolean isAfternoon_Report() {
        return Afternoon_Report;
    }

    public void setAfternoon_Report(boolean afternoon_Report) {
        Afternoon_Report = afternoon_Report;
    }

    public boolean isAfternoon_Eat() {
        return Afternoon_Eat;
    }

    public void setAfternoon_Eat(boolean afternoon_Eat) {
        Afternoon_Eat = afternoon_Eat;
    }

    public boolean isEvening_Report() {
        return Evening_Report;
    }

    public void setEvening_Report(boolean evening_Report) {
        Evening_Report = evening_Report;
    }

    public boolean isEvening_Eat() {
        return Evening_Eat;
    }

    public void setEvening_Eat(boolean evening_Eat) {
        Evening_Eat = evening_Eat;
    }
}
