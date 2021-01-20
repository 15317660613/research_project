package com.adc.da.finance.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * <b>功能：</b>F_RESEARCH_ISSUE ResearchIssueEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class ResearchIssueEO extends BaseEntity {

    private String id;
    private String issueName;
    private String issueNo;
    private String orgId;
    private Integer status;
    private String rstatus;
    private Integer delFlag;
    private String createUserId;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String updateUserId;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String extInfo;
    private String extInfo2;
    private String extInfo3;

    private String orgName;
    private String statusStr;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>issueName -> issue_name</li>
     * <li>issueNo -> issue_no</li>
     * <li>orgId -> org_id</li>
     * <li>status -> status</li>
     * <li>delFlag -> del_flag</li>
     * <li>createUserId -> create_user_id</li>
     * <li>createTime -> create_time</li>
     * <li>updateUserId -> update_user_id</li>
     * <li>updateTime -> update_time</li>
     * <li>extInfo -> ext_info</li>
     * <li>extInfo2 -> ext_info2</li>
     * <li>extInfo3 -> ext_info3</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "issueName": return "issue_name";
            case "issueNo": return "issue_no";
            case "orgId": return "org_id";
            case "status": return "status";
            case "delFlag": return "del_flag";
            case "createUserId": return "create_user_id";
            case "createTime": return "create_time";
            case "updateUserId": return "update_user_id";
            case "updateTime": return "update_time";
            case "extInfo": return "ext_info";
            case "extInfo2": return "ext_info2";
            case "extInfo3": return "ext_info3";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>issue_name -> issueName</li>
     * <li>issue_no -> issueNo</li>
     * <li>org_id -> orgId</li>
     * <li>status -> status</li>
     * <li>del_flag -> delFlag</li>
     * <li>create_user_id -> createUserId</li>
     * <li>create_time -> createTime</li>
     * <li>update_user_id -> updateUserId</li>
     * <li>update_time -> updateTime</li>
     * <li>ext_info -> extInfo</li>
     * <li>ext_info2 -> extInfo2</li>
     * <li>ext_info3 -> extInfo3</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "issue_name": return "issueName";
            case "issue_no": return "issueNo";
            case "org_id": return "orgId";
            case "status": return "status";
            case "del_flag": return "delFlag";
            case "create_user_id": return "createUserId";
            case "create_time": return "createTime";
            case "update_user_id": return "updateUserId";
            case "update_time": return "updateTime";
            case "ext_info": return "extInfo";
            case "ext_info2": return "extInfo2";
            case "ext_info3": return "extInfo3";
            default: return null;
        }
    }
}