package com.adc.da.budget.vo;

import com.adc.da.excel.annotation.Excel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DailyDetailExportVO {

    private String id;

    @Excel(name = "一级部门", orderNum = "0" ,width = 30)
    private String firstDeptName;

    @Excel(name = "部门", orderNum = "1" ,width = 30)
    private String thisDeptName;
    //日报创建人
    @Excel(name = "日报创建人", orderNum = "2",width = 10)
    private String createUserName;

    //业务名称
    @Excel(name = "业务名称", orderNum = "3",width = 30)
    private String budgetName ;

    //业务名称
    @Excel(name = "业务编号", orderNum = "4",width = 30)
    private String budgetNO ;

    @Excel(name = "项目名称", orderNum = "5" ,width = 30)
    private String projectName ;
    //项目编号
    @Excel(name = "项目编号", orderNum = "6",width = 30)
    private String projectNO;

    @Excel(name = "任务名称", orderNum = "7",width = 40)
    private  String taskName ; //任务名称

    @Excel(name = "子任务名称", orderNum = "8",width = 40)
    private String childrenTaskName;

    @Excel(name = "日报时间", orderNum = "9",exportFormat = "yyyy-MM-dd",importFormat = "yyyy-MM-dd",width = 20)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dailyCreateTime; //哪天的日报

    @Excel(name = "工时/小时", orderNum = "10",width = 10)
    private Float workCostTime ; // 工时

    @Excel(name = "审批状态", orderNum = "11",width = 10)
    private String status;

    @Excel(name = "审批人", orderNum = "12",width = 10)
    private String approveUserName ; //审批人用户名

    //工作描述
    @Excel(name = "描述", orderNum = "13",width = 100)
    private String workDescription;

}
