package com.adc.da.oa.dto;

import com.adc.da.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * describe:
 * 用于EXCEL批量导入项目的实体类
 *
 * @author 李坤澔
 *     date 2019-05-27
 */
@Getter
@Setter
public class ProjectOADTO {

    /**
     * 序号不会存入数据库，但是导入失败会返回相应的值，若为空则结束导入
     */
    @Excel(name = "编号", orderNum = "1")
    private String excelId;

    /**
     * 合同编号
     */
    @Excel(name = "合同编号", orderNum = "2")
    private String contractNo;

    /**
     * 项目名称
     */
    @Excel(name = "合同名称", orderNum = "3")
    private String projectName;

    /**
     * 业务方
     */
    @Excel(name = "客户名称", orderNum = "4")
    private String projectOwner;

    /**
     * 项目负责人-名称
     */
    @Excel(name = "区域经理", orderNum = "5")
    private String projectLeader;

    /**
     * 开始时间
     */
    @Excel(name = "签订日期", importFormat = "yyyy-MM-dd", orderNum = "6")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date projectStartTime;

    /**
     * 开始时间
     */
    @Excel(name = "生效日期", importFormat = "yyyy-MM-dd", orderNum = "7")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date projectBeginTime;

    /**
     * 结束时间
     */
    @Excel(name = "终止日期", importFormat = "yyyy-MM-dd", orderNum = "8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date projectEndTime;

    /**
     * 所属部门
     */
    @Excel(name = "业务部门", orderNum = "9")
    private String deptName;

    /**
     * 所属业务 编号（oa项目）
     */
    @Excel(name = "项目编号1", orderNum = "10")
    private String budgetDomainId;

    /**
     * 所属业务
     */
    @Excel(name = "项目名称1", orderNum = "11")
    private String budgetName;

    /**
     * 项目合同金额
     */
    @Excel(name = "项目合同金额", orderNum = "12")
    private String contractAmount;

    /**
     * 合同编号+项目编号
     */
    @Excel(name = "合同编号+项目编号", orderNum = "13")
    private String contractNoAndDomainId;

    /**
     * 所属板块
     */
    @Excel(name = "所属板块", orderNum = "14")
    private String type;
}
