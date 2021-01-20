package com.adc.da.progress.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <b>功能：</b>PR_PROJECT_BUDGET_CHANGE ProjectBudgetChangeEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class ProjectBudgetChangeEO extends BaseEntity {

    //id
    private String id;

    //人员成本
    private BigDecimal personCost;

    //采购成本
    private BigDecimal purchaseCost;

    //协作成本
    private BigDecimal cooperationCost;

    //其他成本
    private BigDecimal otherCost;

    //合计
    private BigDecimal amountCount;

    //变更版本
    private Integer changeVersion = 0;

    private String extInfo1;

    private String extInfo2;

    private String extInfo3;

    private String extInfo4;

    private String extInfo5;

    private String projectName;

    //项目id
    private String projectId;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>personCost -> person_cost</li>
     * <li>purchaseCost -> purchase_cost</li>
     * <li>cooperationCost -> cooperation_cost</li>
     * <li>otherCost -> other_cost</li>
     * <li>amountCount -> amount_count</li>
     * <li>changeVersion -> change_version</li>
     * <li>extInfo1 -> ext_info1</li>
     * <li>extInfo2 -> ext_info2</li>
     * <li>extInfo3 -> ext_info3</li>
     * <li>extInfo4 -> ext_info4</li>
     * <li>extInfo5 -> ext_info5</li>
     * <li>projectName -> project_name</li>
     * <li>projectId -> project_id</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id":
                return "id";
            case "personCost":
                return "person_cost";
            case "purchaseCost":
                return "purchase_cost";
            case "cooperationCost":
                return "cooperation_cost";
            case "otherCost":
                return "other_cost";
            case "amountCount":
                return "amount_count";
            case "changeVersion":
                return "change_version";
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
            case "projectName":
                return "project_name";
            case "projectId":
                return "project_id";
            default:
                return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>person_cost -> personCost</li>
     * <li>purchase_cost -> purchaseCost</li>
     * <li>cooperation_cost -> cooperationCost</li>
     * <li>other_cost -> otherCost</li>
     * <li>amount_count -> amountCount</li>
     * <li>change_version -> changeVersion</li>
     * <li>ext_info1 -> extInfo1</li>
     * <li>ext_info2 -> extInfo2</li>
     * <li>ext_info3 -> extInfo3</li>
     * <li>ext_info4 -> extInfo4</li>
     * <li>ext_info5 -> extInfo5</li>
     * <li>project_name -> projectName</li>
     * <li>project_id -> projectId</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id":
                return "id";
            case "person_cost":
                return "personCost";
            case "purchase_cost":
                return "purchaseCost";
            case "cooperation_cost":
                return "cooperationCost";
            case "other_cost":
                return "otherCost";
            case "amount_count":
                return "amountCount";
            case "change_version":
                return "changeVersion";
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
            case "project_name":
                return "projectName";
            case "project_id":
                return "projectId";
            default:
                return null;
        }
    }

}
