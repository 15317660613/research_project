package com.adc.da.group.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>TS_CUSTOM_GROUP CustomGroupEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-25 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class CustomGroupEO extends BaseEntity {

    private String id;

    private String createUserId;

    private String groupName;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String extInfo1;

    private String extInfo2;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id_</li>
     * <li>createUserId -> create_user_id_</li>
     * <li>groupName -> group_name_</li>
     * <li>createTime -> create_time_</li>
     * <li>extInfo1 -> ext_info_1_</li>
     * <li>extInfo2 -> ext_info_2_</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {
            return null;
        }
        switch (fieldName) {
            case "id":
                return "id_";
            case "createUserId":
                return "create_user_id_";
            case "groupName":
                return "group_name_";
            case "createTime":
                return "create_time_";
            case "extInfo1":
                return "ext_info_1_";
            case "extInfo2":
                return "ext_info_2_";
            default:
                return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id_ -> id</li>
     * <li>create_user_id_ -> createUserId</li>
     * <li>group_name_ -> groupName</li>
     * <li>create_time_ -> createTime</li>
     * <li>ext_info_1_ -> extInfo1</li>
     * <li>ext_info_2_ -> extInfo2</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {
            return null;
        }
        switch (columnName) {
            case "id_":
                return "id";
            case "create_user_id_":
                return "createUserId";
            case "group_name_":
                return "groupName";
            case "create_time_":
                return "createTime";
            case "ext_info_1_":
                return "extInfo1";
            case "ext_info_2_":
                return "extInfo2";
            default:
                return null;
        }
    }

    /**
     *
     **/
    public String getId() {
        return this.id;
    }

    /**
     *
     **/
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     **/
    public String getCreateUserId() {
        return this.createUserId;
    }

    /**
     *
     **/
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**
     *
     **/
    public String getGroupName() {
        return this.groupName;
    }

    /**
     *
     **/
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     *
     **/
    public Date getCreateTime() {
        return this.createTime;
    }

    /**
     *
     **/
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     *
     **/
    public String getExtInfo1() {
        return this.extInfo1;
    }

    /**
     *
     **/
    public void setExtInfo1(String extInfo1) {
        this.extInfo1 = extInfo1;
    }

    /**
     *
     **/
    public String getExtInfo2() {
        return this.extInfo2;
    }

    /**
     *
     **/
    public void setExtInfo2(String extInfo2) {
        this.extInfo2 = extInfo2;
    }

}
