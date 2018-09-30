package com.hd.attendance.activity.fingerprint.bean;

/**
 * Created by 蒋 on 2018/9/22.
 * 用户指纹
 */

public class UserFingerBean {
    private int FingerID;//指纹ID
    private int UserId;//员工ID
    private String UserNmae;//员工姓名
    private int GroupID;//小组ID
    private String GroupName;//小组名称
    private String FingerState;//指纹状态

    public int getFingerID() {
        return FingerID;
    }

    public void setFingerID(int fingerID) {
        FingerID = fingerID;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUserNmae() {
        return UserNmae;
    }

    public void setUserNmae(String userNmae) {
        UserNmae = userNmae;
    }

    public int getGroupID() {
        return GroupID;
    }

    public void setGroupID(int groupID) {
        GroupID = groupID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getFingerState() {
        return FingerState;
    }

    public void setFingerState(String fingerState) {
        FingerState = fingerState;
    }

    @Override
    public String toString() {
        return "{" +
                "FingerID=" + FingerID +
                ", UserId=" + UserId +
                ", UserNmae='" + UserNmae + '\'' +
                ", GroupID=" + GroupID +
                ", GroupName='" + GroupName + '\'' +
                ", FingerState='" + FingerState + '\'' +
                '}';
    }
}
