package com.hd.attendance.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by 蒋 on 2018/10/20.
 * 考勤记录表
 * 每个员工 每天 只能有一条记录
 */

public class AttendancemTable extends LitePalSupport {
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
    private boolean isDeductions=false;//是否有扣款
    @Column
    private double deductions=0;//扣款金额
    @Column
    private String deductionsInfo="无";//扣款说明


    @Column(defaultValue = "1")
    private int MorningWorkType;//上午出勤情况: 正常上班 请假  旷工  加班  出差   默认正常上班
    //上午-上班打卡相关字段
    @Column
    private String morning_start_time;//上午上班打卡时间
    @Column
    private int morning_start_type;//上午上班打卡类型 从AttendType里面获取
    @Column
    private String morning_start_note="无";//上午上班 备注内容

    //上午-下班 打卡相关字段
    @Column
    private String morning_end_time;//上午下班打卡时间
    @Column
    private int morning_end_type;//上午下班打卡类型 从AttendType里面获取
    @Column
    private String morning_end_note="无";//上下上班 备注


    @Column(defaultValue = "1")
    private int AfternoonWorkType;//下午出勤情况

    //下午-上班打卡相关字段
    @Column
    private String afternoon_start_time;//下午上班打卡时间
    @Column
    private int afternoon_start_type;//下午上班打卡类型 从AttendType里面获取
    @Column
    private String afternoon_start_note="无";//下午上班 备注

    //下午-下班 打卡相关字段
    @Column
    private String afternoon_end_time;//下午-下班打卡时间
    @Column
    private int afternoon_end_type;//下午-下班打卡类型 从AttendType里面获取
    @Column
    private String afternoon_end_note="无";//下午-下班 备注内容


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


    public String getMorning_start_time() {
        return morning_start_time;
    }

    public void setMorning_start_time(String morning_start_time) {
        this.morning_start_time = morning_start_time;
    }

    public int getMorning_start_type() {
        return morning_start_type;
    }

    public void setMorning_start_type(int morning_start_type) {
        this.morning_start_type = morning_start_type;
    }

    public String getMorning_start_note() {
        return morning_start_note;
    }

    public void setMorning_start_note(String morning_start_note) {
        this.morning_start_note = morning_start_note;
    }

    public String getMorning_end_time() {
        return morning_end_time;
    }

    public void setMorning_end_time(String morning_end_time) {
        this.morning_end_time = morning_end_time;
    }

    public int getMorning_end_type() {
        return morning_end_type;
    }

    public void setMorning_end_type(int morning_end_type) {
        this.morning_end_type = morning_end_type;
    }

    public String getMorning_end_note() {
        return morning_end_note;
    }

    public void setMorning_end_note(String morning_end_note) {
        this.morning_end_note = morning_end_note;
    }

    public String getAfternoon_start_time() {
        return afternoon_start_time;
    }

    public void setAfternoon_start_time(String afternoon_start_time) {
        this.afternoon_start_time = afternoon_start_time;
    }

    public int getAfternoon_start_type() {
        return afternoon_start_type;
    }

    public void setAfternoon_start_type(int afternoon_start_type) {
        this.afternoon_start_type = afternoon_start_type;
    }

    public String getAfternoon_start_note() {
        return afternoon_start_note;
    }

    public void setAfternoon_start_note(String afternoon_start_note) {
        this.afternoon_start_note = afternoon_start_note;
    }

    public String getAfternoon_end_time() {
        return afternoon_end_time;
    }

    public void setAfternoon_end_time(String afternoon_end_time) {
        this.afternoon_end_time = afternoon_end_time;
    }

    public int getAfternoon_end_type() {
        return afternoon_end_type;
    }

    public void setAfternoon_end_type(int afternoon_end_type) {
        this.afternoon_end_type = afternoon_end_type;
    }

    public String getAfternoon_end_note() {
        return afternoon_end_note;
    }

    public void setAfternoon_end_note(String afternoon_end_note) {
        this.afternoon_end_note = afternoon_end_note;
    }


    public boolean isDeductions() {
        return isDeductions;
    }

    public void setIsDeductions(boolean deductions) {
        this.isDeductions = deductions;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public String getDeductionsInfo() {
        return deductionsInfo;
    }

    public void setDeductionsInfo(String deductionsInfo) {
        this.deductionsInfo = deductionsInfo;
    }

    public int getId() {
        return id;
    }


    public int getMorningWorkType() {
        return MorningWorkType;
    }

    public void setMorningWorkType(int morningWorkType) {
        MorningWorkType = morningWorkType;
    }

    public int getAfternoonWorkType() {
        return AfternoonWorkType;
    }

    public void setAfternoonWorkType(int afternoonWorkType) {
        AfternoonWorkType = afternoonWorkType;
    }
}
