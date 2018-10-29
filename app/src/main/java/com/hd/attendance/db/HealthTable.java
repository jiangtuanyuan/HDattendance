package com.hd.attendance.db;


import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by 蒋 on 2018/10/27.
 * 卫生表
 */

public class HealthTable extends LitePalSupport {
    @Column
    private int id; //不可构造set方法 自增ID
    @Column
    private int weekid; //1 2 3 4 5 6 7  只能1到7
    @Column
    private String weeki; //对应上面的 星期一 星期二....

    @Column
    private String OnFloorUserID;//一楼的用户ID
    @Column
    private String OnFloorUserName;//一楼的用户名称
    @Column
    private String OnFloorInfo;//一楼的详情卫生安排

    @Column
    private String TwoFloorUserID;//二楼的用户ID
    @Column
    private String TwoFloorUserName;//二楼的用户名称
    @Column
    private String TwoFloorInfo;//二楼的详情卫生安排


    public int getId() {
        return id;
    }

    public int getWeekid() {
        return weekid;
    }

    public void setWeekid(int weekid) {
        this.weekid = weekid;
    }

    public String getWeeki() {
        return weeki;
    }

    public void setWeeki(String weeki) {
        this.weeki = weeki;
    }

    public String getOnFloorUserID() {
        return OnFloorUserID;
    }

    public void setOnFloorUserID(String onFloorUserID) {
        OnFloorUserID = onFloorUserID;
    }

    public String getOnFloorUserName() {
        return OnFloorUserName;
    }

    public void setOnFloorUserName(String onFloorUserName) {
        OnFloorUserName = onFloorUserName;
    }

    public String getOnFloorInfo() {
        return OnFloorInfo;
    }

    public void setOnFloorInfo(String onFloorInfo) {
        OnFloorInfo = onFloorInfo;
    }

    public String getTwoFloorUserID() {
        return TwoFloorUserID;
    }

    public void setTwoFloorUserID(String twoFloorUserID) {
        TwoFloorUserID = twoFloorUserID;
    }

    public String getTwoFloorUserName() {
        return TwoFloorUserName;
    }

    public void setTwoFloorUserName(String twoFloorUserName) {
        TwoFloorUserName = twoFloorUserName;
    }

    public String getTwoFloorInfo() {
        return TwoFloorInfo;
    }

    public void setTwoFloorInfo(String twoFloorInfo) {
        TwoFloorInfo = twoFloorInfo;
    }
}
