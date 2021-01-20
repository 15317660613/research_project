package com.adc.da.budget.dto;

import com.adc.da.budget.entity.Project;
import com.adc.da.excel.annotation.Excel;
import lombok.Data;

/**
 * describe:
 * 用于EXCEL批量导入项目的实体类
 *
 * @author 李坤澔
 *     date 2019-05-27
 */
@Data
public class ProjectExcelData {

    /**
     * 序号不会存入数据库，但是导入失败会返回相应的值，若为空则结束导入
     */
    @Excel(name = "序号", orderNum = "0")
    private String excelId;

    /**
     * 项目名称
     */
    @Excel(name = "项目名称", orderNum = "1")
    private String name;

    /**
     * 开始时间
     */
    @Excel(name = "开始时间-导入", orderNum = "2")
    private String projectStartTimeStr;


    /**
     * 所属部门
     */
    @Excel(name = "部门", orderNum = "3")
    private String deptName;

    /**
     * 项目负责人-名称
     */
    @Excel(name = "项目负责人", orderNum = "4")
    private String projectLeader;

    /**
     * 业务方
     */
    @Excel(name = "业务方", orderNum = "5")
    private String projectOwner;

    /**
     * 项目组成员
     */
    @Excel(name = "项目组成员", orderNum = "6")
    private String projectMemberNames;

    /**
     * 所属业务
     */
    @Excel(name = "所属业务", orderNum = "7")
    private String budget;

    /**
     * 业务类型
     */
    @Excel(name = "业务类型", orderNum = "8")
    private String business;

    /**
     * 人力投入，记得转换成Integer
     */
    @Excel(name = "人力投入", orderNum = "9")
    private Integer personInput;

    /**
     * 合同金额
     */
    @Excel(name = "合同金额", orderNum = "10")
    private float contractAmount;

    /**
     * 合同编号
     */
    @Excel(name = "合同编号", orderNum = "11")
    private String contractNo;

    /**
     * 项目描述
     */
    @Excel(name = "项目描述", orderNum = "12")
    private String projectDescription;

    /**
     * 开始时间
     */
    @Excel(name = "项目开始时间-导入", orderNum = "13")
    private String projectBeginTimeStr;

    /**
     * 结束时间
     */
    @Excel(name = "项目结束时间-导入", orderNum = "14")
    private String projectEndTimeStr;
}
