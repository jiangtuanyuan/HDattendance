package com.hd.attendance.activity.group.user;

import java.io.Serializable;

/**
 * Created by 蒋 on 2018/7/7.
 * 用户列表数据实体类
 */

public class UserBean implements Serializable {
    private int user_id;//用户ID
    private String user_name;//昵称
    private String user_sex;//性别
    private String jobs;//岗位
    private int group_id;
    private String group_name;
    private boolean checked = false;//是否选择了此用户


    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getJobs() {
        return jobs;
    }

    public void setJobs(String jobs) {
        this.jobs = jobs;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }


    @Override
    public String toString() {
        return "{" +
                "user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", jobs='" + jobs + '\'' +
                ", group_id='" + group_id + '\'' +
                ", group_name='" + group_name + '\'' +
                ", checked=" + checked +
                '}';
    }
}
