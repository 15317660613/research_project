package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Document(indexName = "financial_prd", type = "Daily")
@Setter
@Getter
@ToString
public class Daily extends BaseEntity {
    //日报ID
    @Id
    private String id;

    //所属项目ID)
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String projectId;

    //业务名
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String budgetId ;

    //事件名称
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String eventName;

    //工作描述
    @Excel(name = "工作描述", orderNum = "4")
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String workDescription;

    //处理的任务
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String taskIds;

    private String[] taskIdArray;

    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String taskStatus;

    private String[] taskStatusArray;

    @Excel(name = "日报时间", orderNum = "7",exportFormat = "yyyy-MM-dd",importFormat = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dailyCreateTime; //哪天的日报

    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String projectLeaderId;

    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String deptId;

    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String createUserId;
    //日报创建人
    @Excel(name = "日报创建人", orderNum = "8")
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String createUserName;

    private String workTime; //废弃字段
    //处理任务的工时数据
    private float[] worktimeArray;

    //任务是否完成
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String finishedStatus;

    @Excel(name = "日报类型", orderNum = "11")
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String dailyType;

    @Excel(name = "时间段", orderNum = "12")
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String timeSlot;

    @Excel(name = "客户", orderNum = "13")
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String customer;

    @Excel(name = "客户部门", orderNum = "14")
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String dept;

    @Excel(name = "对接人", orderNum = "15")
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String contacts;

    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String projectTeam;



    private Date createTime; // 创建日报的时间，但是不一定是那天的日报

    private Date modifyTime;
    //删除标记
    private Boolean delFlag;

        /**
        * 1-通过审批
        * 2-审批中
        * 3-驳回/草稿
         *4-待确认 认领后 状态 保存置为3 提交置为2
        */
    @Field
    private Integer approveState ; // 审批状态
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String approveUserId ; //审批人id
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String approveUserName ; //审批人用户名

    private Float workCostTime ; // 工时

    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private  String taskName ; //任务名称
    //项目名
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String projectName ;
    //业务名
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String budgetName ;


    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String approveUserNameES ; //审批人用户名

    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private  String taskNameES ; //任务名称
    //项目名
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String projectNameES ;
    //业务名
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String budgetNameES ;
    //日报创建人
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String createUserNameES;

    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String resultFileName ;
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String taskResultFileId ;

    //日报的参数 如 实习生的都是10;
    private int dailyField = 0;

    private Boolean childTaskFlag; // 是子任务的传true  不是 传false  null 就是共享日报前的老数据

    private String dailyParentId;

    private String dailyParentCreateUserId;// 父日报创建人

    private String dailyParentCreateUserName; // 父日报创建人

    private String dailyComment; //日报备注

    private String[] childrenDailyCreateUserIds; // 子日报人接收人id

    private String[] childrenDailyCreateUserNames; // 子日报人接收人用户名

    private ArrayList<Map<String,String>> userIdDeptNameMapList; //防止悬浮显示部门时候用到


}
