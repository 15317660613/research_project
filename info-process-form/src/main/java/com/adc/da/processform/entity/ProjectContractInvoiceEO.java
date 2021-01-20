package com.adc.da.processform.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <b>功能：</b>PF_PROJECT_CONTRACT_INVOICE ProjectContractInvoiceEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProjectContractInvoiceEO extends BaseEntity {

    private String id;
    private String projectId;
    private String contractId;
    private BigDecimal contractAmount;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date invoiceDate;
    private BigDecimal invoiceAmount;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date recieveMoneyDate;
    private String ext01;//省份
    private String ext02;//开票单位
    private String ext03;//省份（无合同开票）
    private String ext04;
    private String ext05;
    private String ext06;
    private Integer delflag=0;
    private String hasContractFlag;//有无合同
    private String invoiceNo; //开票单据编号
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date contractApplyDate;//申请日期
    private String areaManagerName;//区域经理
    private String areaManagerId;//区域经理ID
    private String contractFiledFlag; // 合同是否归档
    private String contractName;//合同名称
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date estimateReturnDate;//预计回款时间
    private String containsInstrumentationFlag;//是否包含仪器仪表
    private String outStorehouseNo;//出库单编号
    private String belongDeptId;//所属部门id
    private String belongDeptName;//所属部门名称
    private String contractInvoice;//合同开票
    private String remark;//备注
    private String invoiceNumber;//开票笔数
    private String businessOpportunityNo;//商机编号
    private String businessOpportunityName;//商机名称
    private String projectNo;//项目编号
    private String projectName;//项目名称
    private String businessDeptId;//业务部门ID
    private String businessDeptName;//业务部门
    private String projectManagerId;//项目经理ID
    private String projectManagerName;//项目经理
    private BigDecimal actualInvoiceAmount;//实际开票金额
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date actualInvoiceDate;//实际开票时间
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date changeInvoiceDate;//换票时间
    private BigDecimal changeInvoiceAmount;//换票金额
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date backInvoiceDate;//退票时间
    private BigDecimal backInvoiceAmount;//退票金额
    private BigDecimal originInvoiceAmount;//原开票金额
    private String advanceInvoiceFlag;//是否预开发票
    private String advanceInvoiceReason;//预开发票申请原因
    private String invoiceType;//开票类型
    // 下面两个字段 只做查询处理时使用
    private int year;
    private int month;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projectId -> project_id</li>
     * <li>contractId -> contract_id</li>
     * <li>contractAmount -> contract_amount</li>
     * <li>invoiceDate -> invoice_date</li>
     * <li>invoiceAmount -> invoice_amount</li>
     * <li>recieveMoneyDate -> recieve_money_date</li>
     * <li>ext01 -> ext_01</li>
     * <li>ext02 -> ext_02</li>
     * <li>ext03 -> ext_03</li>
     * <li>ext04 -> ext_04</li>
     * <li>ext05 -> ext_05</li>
     * <li>ext06 -> ext_06</li>
     * <li>delflag -> delflag</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "projectId": return "project_id";
            case "contractId": return "contract_id";
            case "contractAmount": return "contract_amount";
            case "invoiceDate": return "invoice_date";
            case "invoiceAmount": return "invoice_amount";
            case "recieveMoneyDate": return "recieve_money_date";
            case "ext01": return "ext_01";
            case "ext02": return "ext_02";
            case "ext03": return "ext_03";
            case "ext04": return "ext_04";
            case "ext05": return "ext_05";
            case "ext06": return "ext_06";
            case "delflag": return "delflag";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>project_id -> projectId</li>
     * <li>contract_id -> contractId</li>
     * <li>contract_amount -> contractAmount</li>
     * <li>invoice_date -> invoiceDate</li>
     * <li>invoice_amount -> invoiceAmount</li>
     * <li>recieve_money_date -> recieveMoneyDate</li>
     * <li>ext_01 -> ext01</li>
     * <li>ext_02 -> ext02</li>
     * <li>ext_03 -> ext03</li>
     * <li>ext_04 -> ext04</li>
     * <li>ext_05 -> ext05</li>
     * <li>ext_06 -> ext06</li>
     * <li>delflag -> delflag</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "project_id": return "projectId";
            case "contract_id": return "contractId";
            case "contract_amount": return "contractAmount";
            case "invoice_date": return "invoiceDate";
            case "invoice_amount": return "invoiceAmount";
            case "recieve_money_date": return "recieveMoneyDate";
            case "ext_01": return "ext01";
            case "ext_02": return "ext02";
            case "ext_03": return "ext03";
            case "ext_04": return "ext04";
            case "ext_05": return "ext05";
            case "ext_06": return "ext06";
            case "delflag": return "delflag";
            default: return null;
        }
    }


}
