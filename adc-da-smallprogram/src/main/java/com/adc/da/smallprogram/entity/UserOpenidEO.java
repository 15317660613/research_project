package com.adc.da.smallprogram.entity;


import com.adc.da.base.entity.BaseEntity;

/**
 * <b>功能：</b>TS_USER_OPENID UserOpenidEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-03-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class UserOpenidEO extends BaseEntity {

    /**
     * 
     */
    private String id;
    /**
     * 
     */
    private String userId;
    /**
     * 
     */
    private String openId;
    /**
     * 
     */
    private Long delFlag;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>userId -> user_id</li>
     * <li>openId -> open_id</li>
     * <li>delFlag -> del_flag</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) return null;
        switch (fieldName) {
            case "id": return "id";
            case "userId": return "user_id";
            case "openId": return "open_id";
            case "delFlag": return "del_flag";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>user_id -> userId</li>
     * <li>open_id -> openId</li>
     * <li>del_flag -> delFlag</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) return null;
        switch (columnName) {
            case "id": return "id";
            case "user_id": return "userId";
            case "open_id": return "openId";
            case "del_flag": return "delFlag";
            default: return null;
        }
    }
    
    /**
     * get 
     */
    public String getId() {
        return id;
    }

    /**
     * set 
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * get 
     */
    public String getUserId() {
        return userId;
    }

    /**
     * set 
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    /**
     * get 
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * set 
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }
    /**
     * get 
     */
    public Long getDelFlag() {
        return delFlag;
    }

    /**
     * set 
     */
    public void setDelFlag(Long delFlag) {
        this.delFlag = delFlag;
    }
}
