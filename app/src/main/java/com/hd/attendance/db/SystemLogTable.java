package com.hd.attendance.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by 蒋 on 2018/10/6.
 * 系统日志
 */

public class SystemLogTable extends LitePalSupport {
    @Column
    private int id; //不可构造set方法 自增ID
    @Column
    private String date;//时间 年月日 每天只能一条
    @Column
    private String info;//相关信息例如以 [12:20] xxx打了上班卡


    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
