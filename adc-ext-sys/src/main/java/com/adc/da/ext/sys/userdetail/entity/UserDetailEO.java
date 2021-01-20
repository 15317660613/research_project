package com.adc.da.ext.sys.userdetail.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>TS_USER_DETAIL UserDetailEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-09-27 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class UserDetailEO extends BaseEntity {

    private String id;
    private String userId;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatePwdTime;
    private Integer frozenFlag;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>userId -> user_id</li>
     * <li>updatePwdTime -> update_pwd_time</li>
     * <li>frozenFlag -> frozen_flag</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "userId": return "user_id";
            case "updatePwdTime": return "update_pwd_time";
            case "frozenFlag": return "frozen_flag";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>user_id -> userId</li>
     * <li>update_pwd_time -> updatePwdTime</li>
     * <li>frozen_flag -> frozenFlag</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "user_id": return "userId";
            case "update_pwd_time": return "updatePwdTime";
            case "frozen_flag": return "frozenFlag";
            default: return null;
        }
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
    public String getUserId() {
        return this.userId;
    }

    /**  **/
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**  **/
    public Date getUpdatePwdTime() {
        return this.updatePwdTime;
    }

    /**  **/
    public void setUpdatePwdTime(Date updatePwdTime) {
        this.updatePwdTime = updatePwdTime;
    }

    /**  **/
    public Integer getFrozenFlag() {
        return this.frozenFlag;
    }

    /**  **/
    public void setFrozenFlag(Integer frozenFlag) {
        this.frozenFlag = frozenFlag;
    }



}
