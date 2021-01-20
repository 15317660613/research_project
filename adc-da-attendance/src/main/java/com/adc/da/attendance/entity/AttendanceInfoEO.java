package com.adc.da.attendance.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>ATTENDANCE_INFO AttendanceInfoEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-01-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class AttendanceInfoEO extends BaseEntity implements Comparable<AttendanceInfoEO> {

    private String id;

    private String workId;

    private String humanName;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date attendance;

    private String attendanceTime;

    private String dept;

    private String isdel;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    private String createdUser;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    private String modifyedUser;

    private String isexcept;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>workId -> work_id</li>
     * <li>humanName -> human_name</li>
     * <li>attendance -> attendance</li>
     * <li>attendanceTime -> attendance_time</li>
     * <li>dept -> dept</li>
     * <li>isdel -> isdel</li>
     * <li>createdTime -> created_time</li>
     * <li>createdUser -> created_user</li>
     * <li>modifiedTime -> modified_time</li>
     * <li>modifyedUser -> modifyed_user</li>
     * <li>isexcept -> isexcept</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {
            return null;
        }
        switch (fieldName) {
            case "id":
                return "id";
            case "workId":
                return "work_id";
            case "humanName":
                return "human_name";
            case "attendance":
                return "attendance";
            case "attendanceTime":
                return "attendance_time";
            case "dept":
                return "dept";
            case "isdel":
                return "isdel";
            case "createdTime":
                return "created_time";
            case "createdUser":
                return "created_user";
            case "modifiedTime":
                return "modified_time";
            case "modifyedUser":
                return "modifyed_user";
            case "isexcept":
                return "isexcept";
            default:
                return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>work_id -> workId</li>
     * <li>human_name -> humanName</li>
     * <li>attendance -> attendance</li>
     * <li>attendance_time -> attendanceTime</li>
     * <li>dept -> dept</li>
     * <li>isdel -> isdel</li>
     * <li>created_time -> createdTime</li>
     * <li>created_user -> createdUser</li>
     * <li>modified_time -> modifiedTime</li>
     * <li>modifyed_user -> modifyedUser</li>
     * <li>isexcept -> isexcept</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "id":
                return "id";
            case "work_id":
                return "workId";
            case "human_name":
                return "humanName";
            case "attendance":
                return "attendance";
            case "attendance_time":
                return "attendanceTime";
            case "dept":
                return "dept";
            case "isdel":
                return "isdel";
            case "created_time":
                return "createdTime";
            case "created_user":
                return "createdUser";
            case "modified_time":
                return "modifiedTime";
            case "modifyed_user":
                return "modifyedUser";
            case "isexcept":
                return "isexcept";
            default:
                return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttendanceInfoEO)) {
            return false;
        }
        AttendanceInfoEO that = (AttendanceInfoEO) o;
        if (getWorkId() != null ? !getWorkId().equals(that.getWorkId()) : that.getWorkId() != null) {
            return false;
        }
        if (getHumanName() != null ? !getHumanName().equals(that.getHumanName()) : that.getHumanName() != null) {
            return false;
        }
        return getAttendance() != null ? getAttendance().equals(that.getAttendance()) : that.getAttendance() == null;
    }

    @Override
    public int hashCode() {
        int result = getWorkId() != null ? getWorkId().hashCode() : 0;
        result = 31 * result + (getHumanName() != null ? getHumanName().hashCode() : 0);
        result = 31 * result + (getAttendance() != null ? getAttendance().hashCode() : 0);
        return result;
    }

    /**  **/
    public String getId() {
        return this.id;
    }

    /**  **/
    public void setId(String id) {
        this.id = id;
    }

    /**  **/
    public String getWorkId() {
        return this.workId;
    }

    /**  **/
    public void setWorkId(String workId) {
        this.workId = workId;
    }

    /**  **/
    public String getHumanName() {
        return this.humanName;
    }

    /**  **/
    public void setHumanName(String humanName) {
        this.humanName = humanName;
    }

    /**  **/
    public Date getAttendance() {
        return this.attendance;
    }

    /**  **/
    public void setAttendance(Date attendance) {
        this.attendance = attendance;
    }

    /**  **/
    public String getAttendanceTime() {
        return this.attendanceTime;
    }

    /**  **/
    public void setAttendanceTime(String attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    /**  **/
    public String getDept() {
        return this.dept;
    }

    /**  **/
    public void setDept(String dept) {
        this.dept = dept;
    }

    /**  **/
    public String getIsdel() {
        return this.isdel;
    }

    /**  **/
    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    /**  **/
    public Date getCreatedTime() {
        return this.createdTime;
    }

    /**  **/
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**  **/
    public String getCreatedUser() {
        return this.createdUser;
    }

    /**  **/
    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    /**  **/
    public Date getModifiedTime() {
        return this.modifiedTime;
    }

    /**  **/
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    /**  **/
    public String getModifyedUser() {
        return this.modifyedUser;
    }

    /**  **/
    public void setModifyedUser(String modifyedUser) {
        this.modifyedUser = modifyedUser;
    }

    /**  **/
    public String getIsexcept() {
        return this.isexcept;
    }

    /**  **/
    public void setIsexcept(String isexcept) {
        this.isexcept = isexcept;
    }

    @Override
    public int compareTo(AttendanceInfoEO o) {
        return Integer.valueOf(this.workId) - Integer.valueOf(o.workId);
    }
}
