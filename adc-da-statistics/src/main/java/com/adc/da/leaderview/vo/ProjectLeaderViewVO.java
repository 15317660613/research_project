package com.adc.da.leaderview.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ProjectLeaderViewVO {
    private String projectId ;  // 项目id
    private String projectName ; // 项目名称
    private String projectLeader ; // 项目负责人姓名
    private Date projectCreateDate ; //项目开始时间
    private Date projectEndTime ; //项目结束时时间
    private String projectMemberNames ; //项目组成员
    private String projectStatus ; //项目状态
    private String projectWorkTime ; //项目工时

    private String processPercent="0.00%";

    private String projectContractAmount; //合同额
    private String projectInvoiceAmount;
    private String projectMoney ;
    private String projectProfit ;


    private String projectTotalReceivable;
    private String projectTotalIncome;
    private String projectTotalExpenditure;



}
