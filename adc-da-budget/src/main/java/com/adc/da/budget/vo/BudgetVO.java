package com.adc.da.budget.vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class BudgetVO {

    private String id;
    private String deptId;
    @NotBlank(message = "业务名称不能为空！")
    private String projectName;
    /**
     * 项目组名称
     */
    @NotBlank(message = "项目组名称不能为空！")
    private String teamName;
    /**
     * 项目性质
     */
    @NotEmpty(message = "业务性质不能为空")
    private String propertyId;
    /**
     * 项目经理ID
     */
    @NotEmpty(message = "项目经理不能为空")
    private String pm;
    /**
     * 项目经理名称
     */
    private String usname;
    /**
     * 项目所属平台/领域
     */
    private String domainId;

    /**
     * 周期
     */
    @NotBlank(message = "业务周期不能为空！")
    private String cycle;

    /**
     * 周期开始
     */
//    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cycleBegin;
    /**
     * 周期结束
     */
//    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cycleEnd;
    /**
     * 当前年份
     */
    private String currentYear;
    /**
     * 当前年份实际收入
     */
    private Double currentYearEstimate;
    /**
     * 当前年份实际合同
     */
    private Double currentYearDeal;
    /**
     * 每个月的合同，预算
     */
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


    /**
     * 备注
     */
    @Size(max = 255, message = "备注长度不能超过255字符")
    private String remark;
    /**
     * 部门名称
     */
    private String deptName;
//    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /**
     * 创建时间
     */
    private Date createTime;
//    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 下年度合同预算
     */
    private Double nextYearDeal;
    /**
     * 下年度收入预算
     */
    private Double nextYearIncome;

    /**
     * 业务属性
     */
    @NotEmpty(message = "业务属性不能为空")
    private String property;
    /**
     * 项目组
     */
    private String projectTeam;
    /**
     * 业务创建人
     */
    private String createUserId;

    /**
     * 当前用户是否对当前业务具有操作权限
     */
    private Boolean manager;


    private String businessAdminId ;

    private String businessAdminName ;
}
