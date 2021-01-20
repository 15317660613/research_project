package com.adc.da.group.entity;

import com.adc.da.base.entity.BaseEntity;

/**
 * <b>功能：</b>TR_USER_CUSTOM UserCustomEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-25 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class UserCustomEO extends BaseEntity {

    private String userId;

    private String groupId;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>userId -> user_id_</li>
     * <li>groupId -> group_id_</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {
            return null;
        }
        switch (fieldName) {
            case "userId":
                return "user_id_";
            case "groupId":
                return "group_id_";
            default:
                return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>user_id_ -> userId</li>
     * <li>group_id_ -> groupId</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {
            return null;
        }
        switch (columnName) {
            case "user_id_":
                return "userId";
            case "group_id_":
                return "groupId";
            default:
                return null;
        }
    }

    /**
     *
     **/
    public String getUserId() {
        return this.userId;
    }

    /**
     *
     **/
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     **/
    public String getGroupId() {
        return this.groupId;
    }

    /**
     *
     **/
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
