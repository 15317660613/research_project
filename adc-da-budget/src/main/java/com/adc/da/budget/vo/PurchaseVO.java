package com.adc.da.budget.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author chenhaidong
 */
@Data
public class PurchaseVO{

    @ApiModelProperty("项目ID")
    private String id;

    @ApiModelProperty("上会时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @NotNull(message = "上会时间不能为空！")
    private Date meetingTime;

    @ApiModelProperty("项目ID")
    @NotBlank(message = "项目不能为空！")
    private String projectId;

    private String projectName;

    @ApiModelProperty("项目特性")
    @NotBlank(message = "项目特性不能为空！")
    private String projectFeature;

//    @ApiModelProperty("负责人ID")
//    @NotBlank(message = "负责人ID不能为空！")
//    private String principalId;
//
//    @ApiModelProperty("负责人")
//    @NotBlank(message = "负责人不能为空！")
//    private String principal;

    @ApiModelProperty("最大支出金额（万元）")
    @NotNull(message = "最大支出金额不能为空！")
    private Double maxExpenditureAmount;

    @ApiModelProperty("合同类型")
    @NotBlank(message = "合同类型不能为空！")
    private String contractType;

    @ApiModelProperty("预计启动时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @NotNull(message = "预计启动时不能为空！")
    private Date expectedStartTime;

    @ApiModelProperty("项目结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @NotNull(message = "项目结束时间不能为空！")
    private Date projectEndTime;

    @ApiModelProperty("本年度投入（万元）")
    @NotNull(message = "本年度投入不能为空！")
    private Double annualInvestment;

    @ApiModelProperty("下年度投入（万元）")
    @NotNull(message = "下年度投入不能为空！")
    private Double nextAnnualInvestment;

//    @ApiModelProperty("部门ID")
//    @NotBlank(message = "部门ID不能为空！")
//    private String orgId;
//
//    @ApiModelProperty("部门")
//    @NotBlank(message = "部门不能为空！")
//    private String org;

//    @ApiModelProperty("本部ID")
//    @NotBlank(message = "本部ID不能为空！")
//    private String parentOrgId;
//
//    @ApiModelProperty("本部")
//    @NotBlank(message = "本部不能为空！")
//    private String parentOrg;

    @ApiModelProperty("采购方式")
    @NotBlank(message = "采购方式不能为空！")
    private String purchaseType;

    @ApiModelProperty("项目说明")
    private String remark;

}
