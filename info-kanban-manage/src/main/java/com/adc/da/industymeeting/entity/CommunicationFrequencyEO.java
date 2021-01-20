package com.adc.da.industymeeting.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>COMMUNICATION_FREQUENCY CommunicationFrequencyEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class CommunicationFrequencyEO extends BaseEntity {

    private String id;
    private String province;
    private Long frequency;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String createUserId;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String updateUserId;
    private Integer delFlag;
    private String createUser;
    private String updateUser;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>province -> province</li>
     * <li>frequency -> frequency</li>
     * <li>createTime -> create_time</li>
     * <li>createUserId -> create_user_id</li>
     * <li>updateTime -> update_time</li>
     * <li>updateUserId -> update_user_id</li>
     * <li>delFlag -> del_flag</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "province": return "province";
            case "frequency": return "frequency";
            case "createTime": return "create_time";
            case "createUserId": return "create_user_id";
            case "updateTime": return "update_time";
            case "updateUserId": return "update_user_id";
            case "delFlag": return "del_flag";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>province -> province</li>
     * <li>frequency -> frequency</li>
     * <li>create_time -> createTime</li>
     * <li>create_user_id -> createUserId</li>
     * <li>update_time -> updateTime</li>
     * <li>update_user_id -> updateUserId</li>
     * <li>del_flag -> delFlag</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "province": return "province";
            case "frequency": return "frequency";
            case "create_time": return "createTime";
            case "create_user_id": return "createUserId";
            case "update_time": return "updateTime";
            case "update_user_id": return "updateUserId";
            case "del_flag": return "delFlag";
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
    public String getProvince() {
        return this.province;
    }

    /**  **/
    public void setProvince(String province) {
        this.province = province;
    }

    /**  **/
    public Long getFrequency() {
        return this.frequency;
    }

    /**  **/
    public void setFrequency(Long frequency) {
        this.frequency = frequency;
    }

    /**  **/
    public Date getCreateTime() {
        return this.createTime;
    }

    /**  **/
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**  **/
    public String getCreateUserId() {
        return this.createUserId;
    }

    /**  **/
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**  **/
    public Date getUpdateTime() {
        return this.updateTime;
    }

    /**  **/
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**  **/
    public String getUpdateUserId() {
        return this.updateUserId;
    }

    /**  **/
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    /**  **/
    public Integer getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
