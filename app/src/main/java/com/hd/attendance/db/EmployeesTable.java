package com.hd.attendance.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by 蒋 on 2018/9/28.
 * 员工表
 */

public class EmployeesTable extends LitePalSupport {
    @Column
    private int id; //不可构造set方法 自增ID
    @Column
    private String Name;//姓名
    @Column
    private String Sex;//性别
    @Column
    private String jobs;//岗位

    @Column(defaultValue = "0")
    private String administrator;//是否是系统管理员 0:不是  1：是

    @Column(defaultValue = "0")
    private int group_ID;//所属小组 默认0 表示没分组

    public int getGroup_ID() {
        return group_ID;
    }

    public void setGroup_ID(int group_ID) {
        this.group_ID = group_ID;
    }

    public String getName() {
        return Name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        if (sex.equals("男") || sex.equals("女")) {
            this.Sex = sex;
        } else {
            this.Sex = "男";
        }
    }

    public String getAdministrator() {
        return administrator;
    }

    public void setAdministrator(String administrator) {
        if (administrator.equals("0") || administrator.equals("1")) {
            this.administrator = administrator;
        } else {
            this.administrator = "0";
        }
    }

    public String getJobs() {
        return jobs;
    }

    public void setJobs(String jobs) {
        this.jobs = jobs;
    }

    @Override
    public String toString() {

        return "{" +
                "id=" + id +
                ", Name='" + Name + '\'' +
                ", Sex='" + Sex + '\'' +
                ", jobs='" + jobs + '\'' +
                ", group_ID=" + group_ID +
                '}';
    }

}
