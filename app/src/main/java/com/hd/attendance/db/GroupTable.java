package com.hd.attendance.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by 蒋 on 2018/9/29.
 * 组
 */

public class GroupTable extends LitePalSupport {
    @Column
    private int id; //不可构造set方法 自增ID
    @Column
    private String groupName;//组名称
    @Column
    private String groupBoss;//组长
    @Column
    private int groupBoss_ID;//组长ID

    public int getId() {
        return id;
    }


    public int getGroupBoss_ID() {
        return groupBoss_ID;
    }

    public void setGroupBoss_ID(int groupBoss_ID) {
        this.groupBoss_ID = groupBoss_ID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupBoss() {
        return groupBoss;
    }

    public void setGroupBoss(String groupBoss) {
        this.groupBoss = groupBoss;
    }
}
