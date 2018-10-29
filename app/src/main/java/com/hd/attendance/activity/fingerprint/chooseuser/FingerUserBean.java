package com.hd.attendance.activity.fingerprint.chooseuser;

import java.io.Serializable;

/**
 * Created by 蒋 on 2018/10/6.
 *
 */

public class FingerUserBean implements Serializable{
    private int user_id;
    private String user_name;
    private String user_sex;
    private boolean is_finger;//是否录入了指纹
    private boolean checked = false;//是否选择了此用户

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

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

    public boolean isIs_finger() {
        return is_finger;
    }

    public void setIs_finger(boolean is_finger) {
        this.is_finger = is_finger;
    }

    @Override
    public String toString() {
        return "{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", user_sex='" + user_sex + '\'' +
                ", is_finger=" + is_finger +
                ", checked=" + checked +
                '}';
    }
}
