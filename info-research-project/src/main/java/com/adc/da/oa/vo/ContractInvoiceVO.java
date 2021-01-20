package com.adc.da.oa.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-09-18
 */
@Getter
@Setter
public class ContractInvoiceVO {

    /**
     * id
     */
    private String id;

    /**
     * 开票时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date invoiceDate;

    /**
     * 金额
     */
    private BigDecimal invoiceAmount;

    /**
     * 收款金额
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recieveMoneyDate;

    private String ext01;//省份
    private String ext02;//开票单位
    private String ext03;//省份（无合同开票）
    private String hasContractFlag;//有无合同
    private String invoiceNo; //开票单据编号
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date contractApplyDate;//申请日期
   // private String areaManagerName;//区域经理
    private String areaManagerCode;//区域经理ID  工号
    private String contractFiledFlag; // 合同是否归档
    private String contractName;//合同名称
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date estimateReturnDate;//预计回款时间
    private String containsInstrumentationFlag;//是否包含仪器仪表
    private String outStorehouseNo;//出库单编号
    //private String belongDeptId;//所属部门id
    private String belongDeptName;//所属部门名称
    private String contractInvoice;//合同开票
    private String remark;//备注
    private String invoiceNumber;//开票笔数
    private String businessOpportunityNo;//商机编号
    private String businessOpportunityName;//商机名称
    private String projectNo;//项目编号
    private String projectName;//项目名称
    //private String businessDeptId;//业务部门ID
    private String businessDeptName;//业务部门
    private String projectManagerCode;//项目经理ID  工号
    //private String projectManagerName;//项目经理
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
}
