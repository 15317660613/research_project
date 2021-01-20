package com.adc.da.finance.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.adc.da.base.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * <b>功能：</b>F_COST_MANAGEMENT CostManagementEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class CostManagementEO extends BaseEntity implements IExcelModel, IExcelDataModel {

    private String errorMsg;

    private int rowNum;

    private String id;

    @Excel(name = "年", orderNum = "0")
    private Integer year;

    @Excel(name = "月", orderNum = "1")
    private Integer month;

    @Excel(name = "日", orderNum = "2")
    private Integer day;

    @Excel(name = "科目名称", orderNum = "3")
    private String subjectName;

    @Excel(name = "部门", orderNum = "4")
    private String orgName;

    @Excel(name = "凭证号", orderNum = "5")
    private String certificationNumber;

    @Excel(name = "摘要", orderNum = "6")
    private String abstractInfo;

    @Excel(name = "对方科目", orderNum = "7")
    private String otherSubjectName;

    @Excel(name = "金额（元）", orderNum = "8", type = 10)
    private Double amount;

    @Excel(name = "业务", orderNum = "9")
    private String businessName;

    private String businessId;


    private String distributionUserId;

    @Excel(name = "下发人", orderNum = "10")
    private String distributionUserName;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "下发时间", orderNum = "11", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date distributionTime;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "截止认领时间", orderNum = "12", exportFormat = "yyyy/MM/dd", importFormat = "yyyy/MM/dd")
    private Date deadlineTime;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "认领时间", orderNum = "13", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date claimTime;

    @Excel(name = "认领人", orderNum = "14")
    private String claimUserName;
    private String claimUserId;

    @Excel(name = "状态", replace = { "未认领_0", "已认领_1", "逾期未认领_-1" }, orderNum = "15")
    private Integer status;
    private Integer extInfo;
    private Integer extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;
    private String extInfo5;

    private Integer delFlag;

    private String orgId;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     * <li>day -> day</li>
     * <li>subjectName -> subject_name</li>
     * <li>orgName -> org_name</li>
     * <li>certificationNumber -> certification_number</li>
     * <li>abstractInfo -> abstract_info</li>
     * <li>otherSubject -> other_subject</li>
     * <li>amount -> amount</li>
     * <li>businessName -> business_name</li>
     * <li>distributionUserId -> distribution_user_id</li>
     * <li>distributionUserName -> distribution_user_name</li>
     * <li>distributionTime -> distribution_time</li>
     * <li>deadlineTime -> deadline_time</li>
     * <li>claimTime -> claim_time</li>
     * <li>claimUserName -> claim_user_name</li>
     * <li>claimUserId -> claim_user_id</li>
     * <li>status -> status</li>
     * <li>extInfo -> ext_info</li>
     * <li>extInfo1 -> ext_info1</li>
     * <li>extInfo2 -> ext_info2</li>
     * <li>extInfo3 -> ext_info3</li>
     * <li>extInfo4 -> ext_info4</li>
     * <li>extInfo5 -> ext_info5</li>
     * <li>delFlag -> del_flag</li>
     * <li>orgId -> org_id</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
        case "id":
            return "id";
        case "year":
            return "year";
        case "month":
            return "month";
        case "day":
            return "day";
        case "subjectName":
            return "subject_name";
        case "orgName":
            return "org_name";
        case "certificationNumber":
            return "certification_number";
        case "abstractInfo":
            return "abstract_info";
        case "otherSubjectName":
            return "other_subject_name";
        case "amount":
            return "amount";
        case "businessName":
            return "business_name";
        case "distributionUserId":
            return "distribution_user_id";
        case "distributionUserName":
            return "distribution_user_name";
        case "distributionTime":
            return "distribution_time";
        case "deadlineTime":
            return "deadline_time";
        case "claimTime":
            return "claim_time";
        case "claimUserName":
            return "claim_user_name";
        case "claimUserId":
            return "claim_user_id";
        case "status":
            return "status";
        case "extInfo":
            return "ext_info";
        case "extInfo1":
            return "ext_info1";
        case "extInfo2":
            return "ext_info2";
        case "extInfo3":
            return "ext_info3";
        case "extInfo4":
            return "ext_info4";
        case "extInfo5":
            return "ext_info5";
        case "delFlag":
            return "del_flag";
        case "orgId":
            return "org_id";
        case "businessId":
            return "business_id";
        default:
            return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     * <li>day -> day</li>
     * <li>subject_name -> subjectName</li>
     * <li>org_name -> orgName</li>
     * <li>certification_number -> certificationNumber</li>
     * <li>abstract_info -> abstractInfo</li>
     * <li>other_subject -> otherSubject</li>
     * <li>amount -> amount</li>
     * <li>business_name -> businessName</li>
     * <li>distribution_user_id -> distributionUserId</li>
     * <li>distribution_user_name -> distributionUserName</li>
     * <li>distribution_time -> distributionTime</li>
     * <li>deadline_time -> deadlineTime</li>
     * <li>claim_time -> claimTime</li>
     * <li>claim_user_name -> claimUserName</li>
     * <li>claim_user_id -> claimUserId</li>
     * <li>status -> status</li>
     * <li>ext_info -> extInfo</li>
     * <li>ext_info1 -> extInfo1</li>
     * <li>ext_info2 -> extInfo2</li>
     * <li>ext_info3 -> extInfo3</li>
     * <li>ext_info4 -> extInfo4</li>
     * <li>ext_info5 -> extInfo5</li>
     * <li>del_flag -> delFlag</li>
     * <li>org_id -> orgId</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
        case "id":
            return "id";
        case "year":
            return "year";
        case "month":
            return "month";
        case "day":
            return "day";
        case "subject_name":
            return "subjectName";
        case "org_name":
            return "orgName";
        case "certification_number":
            return "certificationNumber";
        case "abstract_info":
            return "abstractInfo";
        case "other_subject_name":
            return "otherSubjectName";
        case "amount":
            return "amount";
        case "business_name":
            return "businessName";
        case "distribution_user_id":
            return "distributionUserId";
        case "distribution_user_name":
            return "distributionUserName";
        case "distribution_time":
            return "distributionTime";
        case "deadline_time":
            return "deadlineTime";
        case "claim_time":
            return "claimTime";
        case "claim_user_name":
            return "claimUserName";
        case "claim_user_id":
            return "claimUserId";
        case "status":
            return "status";
        case "ext_info":
            return "extInfo";
        case "ext_info1":
            return "extInfo1";
        case "ext_info2":
            return "extInfo2";
        case "ext_info3":
            return "extInfo3";
        case "ext_info4":
            return "extInfo4";
        case "ext_info5":
            return "extInfo5";
        case "del_flag":
            return "delFlag";
        case "org_id":
            return "orgId";
        case "business_id":
            return "businessId";
        default:
            return null;
        }
    }
}
