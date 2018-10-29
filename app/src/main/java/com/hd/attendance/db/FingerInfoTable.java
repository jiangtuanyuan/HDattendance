package com.hd.attendance.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by 蒋 on 2018/9/30.
 * 指纹表 用于验证和注册
 */
public class FingerInfoTable extends LitePalSupport {
    @Column
    private int id; //不可构造set方法 自增ID
    @Column
    private int User_ID; //用户ID
    @Column
    private String user_Name; //用户名字
    @Column
    private byte[] finger;//三次合并的指纹 用于验证
    @Column
    private byte[] finger_1;//第一次按下的指纹
    @Column
    private byte[] finger_2;//第二次按下的指纹
    @Column
    private byte[] finger_3;//第三次按下的指纹
    @Column
    private boolean isdance=true;//是否拥有考勤
    @Column
    private boolean ismeal=false;//是否拥有就餐
    @Column
    private int stauts;//指纹状态 0 启用 1 禁止


    public int getStauts() {
        return stauts;
    }

    public void setStauts(int stauts) {
        this.stauts = stauts;
    }

    public boolean isIsdance() {
        return isdance;
    }

    public boolean isIsmeal() {
        return ismeal;
    }

    public boolean getIsdance() {
        return isdance;
    }

    public void setIsdance(boolean isdance) {
        this.isdance = isdance;
    }

    public boolean getIsmeal() {
        return ismeal;
    }

    public void setIsmeal(boolean ismeal) {
        this.ismeal = ismeal;
    }

    public int getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    public String getUser_Name() {
        return user_Name;
    }

    public void setUser_Name(String user_Name) {
        this.user_Name = user_Name;
    }

    public int getId() {
        return id;
    }

    public byte[] getFinger() {
        return finger;
    }

    public void setFinger(byte[] finger) {
        this.finger = finger;
    }

    public byte[] getFinger_1() {
        return finger_1;
    }

    public void setFinger_1(byte[] finger_1) {
        this.finger_1 = finger_1;
    }

    public byte[] getFinger_2() {
        return finger_2;
    }

    public void setFinger_2(byte[] finger_2) {
        this.finger_2 = finger_2;
    }

    public byte[] getFinger_3() {
        return finger_3;
    }

    public void setFinger_3(byte[] finger_3) {
        this.finger_3 = finger_3;
    }
}
