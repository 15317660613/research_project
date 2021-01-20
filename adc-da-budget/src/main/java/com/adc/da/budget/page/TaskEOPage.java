package com.adc.da.budget.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TaskEOPage extends BasePage {

    //任务名称
    private String name;
    private String nameOperator = "=";
    //优先级
    private String priority;
    private String priorityOperator = "=";
    //任务类型
    //参与成员
    //任务计划开始时间
    private String planStartTime;
    private String planStartTime1;
    private String planStartTime1Operator = ">";
    private String planStartTime2;
    private String planStartTime2Operator = "<";
    //任务计划结束时间
    private String planEndTime;
    private String planEndTime1;
    private String planEndTime1Operator = ">";
    private String planEndTime2;
    private String planEndTime2Operator = "<";
    //任务状态
    private String taskStatus;
    private String taskStatusOperator = "=";
    //任务所属业务
    private String budgetId;
    private String budgetIdOperator = "=";
    //任务所属业务
    private String budgetName;
    private String budgetNameOperator = "=";
    //任务所属项目
    private String projectId;
    private String projectIdOperator = "=";

    private String projectName;
    private String projectNameOperator = "=";
    //任务累计人天投入
    //创建人
    private String createUserId;
    private String createUserIdOperator = "=";
    //创建时间
    private String createTime;
    private String createTime1;
    private String createTime1Operator = ">";
    private String createTime2;
    private String createTime2Operator = "<";
    //修改时间
    private String modifyTime;
    private String modifyTime1;
    private String modifyTime1Operator = ">";
    private String modifyTime2;
    private String modifyTime2Operator = "<";
    //任务成员
    private String memberId;
    private String memberIdOperator = "=";

}