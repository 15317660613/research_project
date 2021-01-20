package com.adc.da.budget.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProjectEOPage extends BasePage {

    //项目名称
    private String name;
    private String nameOperator = "=";
    //所属业务ID
    private String budgetId;
    private String budgetIdOperator = "=";
    //项目负责人ID
    private String projectLeaderId;
    private String projectLeaderIdOperator = "=";
    //项目负责人
    private String projectLeader;
    private String projectLeaderOperator = "=";
    //创建时间
    private String startTime;
    private String startTime1;
    private String startTime1Operator = ">";
    private String startTime2;
    private String startTime2Operator = "<";
    //项目状态
    private String finishedStatus;
    private String finishedStatusOperator = "=";
    //人力投入（人/天）
    private Integer personInput;
    private String personInputOperator = "=";
    //业务创建人字段
    private String businessCreateUserId;
    private String businessCreateUserIdOperator = "=";
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
}