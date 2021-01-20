package com.adc.da.business.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * <b>功能：</b>TS_BUDGET BudgetEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-10-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class BudgetEO extends BaseEntity {

    private String id;
    private String deptId;
    private String projectName;
    //项目组名称
    private String teamName;
    //项目性质
    private String propertyId;
    //项目经理ID
    private String pm;
    //项目经理名称
    private String usname;
    //项目所属平台/领域
    private String domainId;

    //周期
    private String cycle;

    //周期开始
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date cycleBegin;
    //周期结束
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date cycleEnd;
    //当前年份
    private String currentYear;
    //当前年份实际收入
    private Double currentYearEstimate;
    //当前年份实际合同
    private Double currentYearDeal;



    private Double nextYear1Deal;
    private Double nextYear1Income;
    private Double nextYear2Deal;
    private Double nextYear2Income;
    private Double nextYear3Deal;
    private Double nextYear3Income;
    private Double nextYear4Deal;
    private Double nextYear4Income;
    private Double nextYear5Deal;
    private Double nextYear5Income;
    private Double nextYear6Deal;
    private Double nextYear6Income;
    private Double nextYear7Deal;
    private Double nextYear7Income;
    private Double nextYear8Deal;
    private Double nextYear8Income;
    private Double nextYear9Deal;
    private Double nextYear9Income;
    private Double nextYear10Deal;
    private Double nextYear10Income;
    private Double nextYear11Deal;
    private Double nextYear11Income;
    private Double nextYear12Deal;
    private Double nextYear12Income;




    //备注
    private String remark;
    private String deptName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    // 下年度合同预算
    private Double nextYearDeal;
    // 下年度收入预算
    private Double nextYearIncome;
    //业务属性
    private String property;
    private String createUserId;

    private String createUserName;

    /**
     * 项目组
     */
    private String projectTeam;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>deptId -> dept_id</li>
     * <li>projectName -> project_name</li>
     * <li>teamName -> team_name</li>
     * <li>propertyId -> property_id</li>
     * <li>pm -> pm</li>
     * <li>domainId -> domain_id</li>
     * <li>cycle -> cycle</li>
     * <li>currentYear -> current_year</li>
     * <li>currentYearEstimate -> current_year_estimate</li>
     * <li>nextYear1Deal -> next_year_1_deal</li>
     * <li>nextYear1Income -> next_year_1_income</li>
     * <li>nextYear2Deal -> next_year_2_deal</li>
     * <li>nextYear2Income -> next_year_2_income</li>
     * <li>nextYear3Deal -> next_year_3_deal</li>
     * <li>nextYear3Income -> next_year_3_income</li>
     * <li>nextYear4Deal -> next_year_4_deal</li>
     * <li>nextYear4Income -> next_year_4_income</li>
     * <li>remark -> remark</li>
     * <li>deptName -> dept_name</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {
            return null;
        }
        switch (fieldName) {
            case "id": return "id";
            case "deptId": return "dept_id";
            case "projectName": return "project_name";
            case "teamName": return "team_name";
            case "propertyId": return "property_id";
            case "pm": return "pm";
            case "domainId": return "domain_id";
            case "cycleBegin": return "cycle_begin";
            case "cycleEnd": return "cycle_end";
            case "currentYear": return "current_year";
            case "currentYearDeal": return "current_year_deal";
            case "currentYearEstimate": return "current_year_estimate";
            case "nextYear1Deal": return "next_year_1_deal";
            case "nextYear1Income": return "next_year_1_income";
            case "nextYear2Deal": return "next_year_2_deal";
            case "nextYear2Income": return "next_year_2_income";
            case "nextYear3Deal": return "next_year_3_deal";
            case "nextYear3Income": return "next_year_3_income";
            case "nextYear4Deal": return "next_year_4_deal";
            case "nextYear4Income": return "next_year_4_income";
            case "nextYear5Deal": return "next_year_5_deal";
            case "nextYear5Income": return "next_year_5_income";
            case "nextYear6Deal": return "next_year_6_deal";
            case "nextYear6Income": return "next_year_6_income";
            case "nextYear7Deal": return "next_year_7_deal";
            case "nextYear7Income": return "next_year_7_income";
            case "nextYear8Deal": return "next_year_8_deal";
            case "nextYear8Income": return "next_year_8_income";
            case "nextYear9Deal": return "next_year_9_deal";
            case "nextYear9Income": return "next_year_9_income";
            case "nextYear10Deal": return "next_year_10_deal";
            case "nextYear10Income": return "next_year_10_income";
            case "nextYear11Deal": return "next_year_11_deal";
            case "nextYear11Income": return "next_year_11_income";
            case "nextYear12Deal": return "next_year_12_deal";
            case "nextYear12Income": return "next_year_12_income";
            case "remark": return "remark";
            case "deptName": return "dept_name";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>dept_id -> deptId</li>
     * <li>project_name -> projectName</li>
     * <li>team_name -> teamName</li>
     * <li>property_id -> propertyId</li>
     * <li>pm -> pm</li>
     * <li>domain_id -> domainId</li>
     * <li>cycle -> cycle</li>
     * <li>current_year -> currentYear</li>
     * <li>current_year_estimate -> currentYearEstimate</li>
     * <li>next_year_1_deal -> nextYear1Deal</li>
     * <li>next_year_1_income -> nextYear1Income</li>
     * <li>next_year_2_deal -> nextYear2Deal</li>
     * <li>next_year_2_income -> nextYear2Income</li>
     * <li>next_year_3_deal -> nextYear3Deal</li>
     * <li>next_year_3_income -> nextYear3Income</li>
     * <li>next_year_4_deal -> nextYear4Deal</li>
     * <li>next_year_4_income -> nextYear4Income</li>
     * <li>remark -> remark</li>
     * <li>dept_name -> deptName</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {
            return null;
        }
        switch (columnName) {
            case "id": return "id";
            case "dept_id": return "deptId";
            case "project_name": return "projectName";
            case "team_name": return "teamName";
            case "property_id": return "propertyId";
            case "pm": return "pm";
            case "domain_id": return "domainId";
            case "cycle_begin": return "cycleBegin";
            case "cycle_end": return "cycleEnd";
            case "current_year": return "currentYear";
            case "current_year_deal": return "currentYearDeal";
            case "current_year_estimate": return "currentYearEstimate";
            case "next_year_1_deal": return "nextYear1Deal";
            case "next_year_1_income": return "nextYear1Income";
            case "next_year_2_deal": return "nextYear2Deal";
            case "next_year_2_income": return "nextYear2Income";
            case "next_year_3_deal": return "nextYear3Deal";
            case "next_year_3_income": return "nextYear3Income";
            case "next_year_4_deal": return "nextYear4Deal";
            case "next_year_4_income": return "nextYear4Income";
            case "next_year_5_deal": return "nextYear5Deal";
            case "next_year_5_income": return "nextYear5Income";
            case "next_year_6_deal": return "nextYear6Deal";
            case "next_year_6_income": return "nextYear6Income";
            case "next_year_7_deal": return "nextYear7Deal";
            case "next_year_7_income": return "nextYear7Income";
            case "next_year_8_deal": return "nextYear8Deal";
            case "next_year_8_income": return "nextYear8Income";
            case "next_year_9_deal": return "nextYear9Deal";
            case "next_year_9_income": return "nextYear9Income";
            case "next_year_10_deal": return "nextYear10Deal";
            case "next_year_10_income": return "nextYear10Income";
            case "next_year_11_deal": return "nextYear11Deal";
            case "next_year_11_income": return "nextYear11Income";
            case "next_year_12_deal": return "nextYear12Deal";
            case "next_year_12_income": return "nextYear12Income";
            case "remark": return "remark";
            case "dept_name": return "deptName";
            default: return null;
        }
    }
}
