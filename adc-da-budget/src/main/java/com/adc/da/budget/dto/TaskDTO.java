package com.adc.da.budget.dto;

import com.adc.da.budget.entity.Task;
import com.adc.da.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class TaskDTO extends Task {

    /**
     * 序号不会存入数据库，但是导入失败会返回相应的值，若为空则结束导入
     */
    @Excel(name = "序号", orderNum = "1")
    private String excelId;

    /**
     * 项目名称
     */
    @Excel(name = "导入-任务名称")
    private String name;

    /**
     *
     */
    @Excel(name = "导入-任务目标")
    private String taskTarget;

    /**
     *
     */
    @Excel(name = "导入-所属业务")
    private String budgetNameExcel;

    /**
     *
     */
    @Excel(name = "导入-所属项目")
    private String projectNameExcel;

    /**
     *
     */
    @Excel(name = "导入-任务负责人")
    private String approveUserNameExcel;

    /**
     * 项目组成员
     */
    @Excel(name = "导入-任务成员")
    private String memberNameExcel;

    /**
     *
     */
    @Excel(name = "导入-开始时间", importFormat = "yyyy-MM-dd")
    private Date timeBeginExcel;

    /**
     *
     */
    @Excel(name = "导入-结束时间", importFormat = "yyyy-MM-dd")
    private Date timeEndExcel;

    /**
     *
     */
    @Excel(name = "导入-优先级")
    private String priority;

    /**
     *
     */
    @Excel(name = "导入-任务预计交付物")
    private String taskResultEOListExcel;

}
