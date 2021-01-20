package com.adc.da.industymeeting.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * <b>功能：</b>INDUSTY_MEETING MeetingEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-03-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class MeetingEO extends BaseEntity {

    private String createUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String delFlag;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
    private String id;
    private String imName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date imDate;
    private String imPlace;
    private String imEnterprise;
    private String departName;
    private String createUserId;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>createUserName -> create_user_name</li>
     * <li>createTime -> create_time</li>
     * <li>delFlag -> del_flag</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     * <li>id -> id</li>
     * <li>imName -> im_name</li>
     * <li>imDate -> im_date</li>
     * <li>imPlace -> im_place</li>
     * <li>imEnterprise` -> im_enterprise`</li>
     * <li>departName -> depart_name</li>
     * <li>createUserId -> create_user_id</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "createUserName": return "create_user_name";
            case "createTime": return "create_time";
            case "delFlag": return "del_flag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            case "id": return "id";
            case "imName": return "im_name";
            case "imDate": return "im_date";
            case "imPlace": return "im_place";
            case "imEnterprise`": return "im_enterprise`";
            case "departName": return "depart_name";
            case "createUserId": return "create_user_id";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>create_user_name -> createUserName</li>
     * <li>create_time -> createTime</li>
     * <li>del_flag -> delFlag</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     * <li>id -> id</li>
     * <li>im_name -> imName</li>
     * <li>im_date -> imDate</li>
     * <li>im_place -> imPlace</li>
     * <li>im_enterprise` -> imEnterprise`</li>
     * <li>depart_name -> departName</li>
     * <li>create_user_id -> createUserId</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "create_user_name": return "createUserName";
            case "create_time": return "createTime";
            case "del_flag": return "delFlag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            case "id": return "id";
            case "im_name": return "imName";
            case "im_date": return "imDate";
            case "im_place": return "imPlace";
            case "im_enterprise`": return "imEnterprise`";
            case "depart_name": return "departName";
            case "create_user_id": return "createUserId";
            default: return null;
        }
    }

}
