package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.budget.annotation.MatchField;
import com.adc.da.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

/**
 * @author qichunxu
 */
@Data
@Document(indexName = "financial_prd", type = "Purchase")
public class Purchase extends BaseEntity {

    /**
     * 采购ID
     */
    @Id
    private String id;

    /**
     * 上会时间
     */
    @Excel(name = "上会时间", orderNum = "1", exportFormat = "yyyy-MM-dd", importFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @MatchField("上会时间")
    private Date meetingTime;

    /**
     * 项目ID
     */
    @MatchField("项目名称")
    private String projectId;

    @Excel(name = "项目名称", orderNum = "2")
    private String projectName;

    /**
     * 项目特性
     */
    @MatchField("项目特性")
    @Excel(name = "项目特性", orderNum = "3")
    private String projectFeature;

    /**
     * 负责人ID
     */
    private String principalId;

    @Excel(name = "负责人", orderNum = "4")
    private String principal;

    /**
     * 最大支出金额（万元）
     */
    @MatchField("最大支出金额")
    @Excel(name = "最大支出金额（万元）", orderNum = "5")
    private String maxExpenditureAmount;

    /**
     * 合同类型
     */
    @MatchField("合同类型")
    @Excel(name = "合同类型", orderNum = "6")
    private String contractType;

    /**
     * 预计启动时间
     */
    @MatchField("预计项目启动时间")
    @Excel(name = "预计启动时间", orderNum = "7", exportFormat = "yyyy-MM-dd", importFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date expectedStartTime;

    /**
     * 项目结束时间
     */
    @MatchField("预计项目结束时间")
    @Excel(name = "项目结束时间", orderNum = "8", exportFormat = "yyyy-MM-dd", importFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date projectEndTime;

    /**
     * 本年度投入（万元）
     */
    @MatchField("本年度投入")
    @Excel(name = "本年度投入（万元）", orderNum = "9")
    private String annualInvestment;

    /**
     * 下年度投入（万元）
     */
    @MatchField("下年度投入")
    @Excel(name = "下年度投入（万元）", orderNum = "10")
    private String nextAnnualInvestment;

    /**
     * 部门ID
     */
    private String orgId;

    @Excel(name = "部门", orderNum = "11")
    private String org;

    /**
     * 本部ID
     */
    private String parentOrgId;

    @Excel(name = "本部", orderNum = "12")
    private String parentOrg;

    /**
     * 采购方式
     */
    @MatchField("采购方式")
    @Excel(name = "采购方式", orderNum = "13")
    private String purchaseType;

    /**
     * 项目说明
     */
    @Excel(name = "项目说明", orderNum = "14")
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;
}
