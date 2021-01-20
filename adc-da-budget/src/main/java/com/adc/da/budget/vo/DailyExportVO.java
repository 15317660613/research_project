package com.adc.da.budget.vo;

import com.adc.da.excel.annotation.Excel;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DailyExportVO {

//    //列表显示创建人、业务、项目、任务、日期、时长、工作描述、审批人
//    //项目名
//    @Excel(name = "项目名", orderNum = "2")
//    private String projectNameES ;
//    //业务名
//    @Excel(name = "业务名", orderNum = "1")
//    private String budgetNameES ;
//    //日报创建人
//    @Excel(name = "日报创建人", orderNum = "0")
//    private String createUserNameES;
//    @Excel(name = "任务名称", orderNum = "3")
//    private  String taskNameES ; //任务名称
//    @Excel(name = "日报时间", orderNum = "4",exportFormat = "yyyy-MM-dd",importFormat = "yyyy-MM-dd")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date dailyCreateTime; //哪天的日报
//    //工作描述
//    @Excel(name = "工作描述", orderNum = "6")
//    private String workDescription;
//    @Excel(name = "工时", orderNum = "5",type = 10)
//    private Float workCostTime ; // 工时
//    @Excel(name = "审批人", orderNum = "7")
//    private String approveUserNameES ; //审批人用户名


    //列表显示创建人、业务、项目、任务、日期、时长、工作描述、审批人
    //项目名
    @Excel(name = "项目名", orderNum = "2" ,width = 30)
    private String projectNameES ;
    //业务名
    @Excel(name = "业务名", orderNum = "1",width = 30)
    private String budgetNameES ;
    //日报创建人
    @Excel(name = "日报创建人", orderNum = "0",width = 10)
    private String createUserNameES;
    @Excel(name = "任务名称", orderNum = "3",width = 40)
    private  String taskNameES ; //任务名称
    @Excel(name = "日报时间", orderNum = "4",exportFormat = "yyyy-MM-dd",importFormat = "yyyy-MM-dd",width = 20)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dailyCreateTime; //哪天的日报
    //工作描述
    @Excel(name = "工作描述", orderNum = "6",width = 100)
    private String workDescription;
    @Excel(name = "工时", orderNum = "5",width = 10)
    private Float workCostTime ; // 工时
    @Excel(name = "审批人", orderNum = "7",width = 10)
    private String approveUserNameES ; //审批人用户名
}
