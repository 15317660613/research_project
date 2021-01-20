package com.adc.da.budget.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class DailyVO {

    @ApiModelProperty("日报ID")
    private String id;

//    @NotBlank(message = "所属项目不能为空！")
    @ApiModelProperty("所属项目ID")
    private String projectId;

    //业务id
    private String budgetId ;


    //项目名
    private String projectName ;
    //业务名
    private String budgetName ;


//    @NotBlank(message = "事件名称不能为空！")
    @ApiModelProperty("事件名称")
    private String eventName;

    @ApiModelProperty("工作描述")
    private String workDescription;

    @NotEmpty(message = "任务列表不能为空！")
    @ApiModelProperty("处理的任务列表")
    private String[] taskIdArray;

//    @NotEmpty(message = "任务处理状态不能为空！")
    @ApiModelProperty("任务处理状态")
    private String[] taskStatusArray;

//    @NotEmpty(message = "投入工时不能为空！")
    @ApiModelProperty("投入工时")
    private Integer[] workTimeArray;

    @ApiModelProperty("日报创建人ID")
    private String createUserId;

//    @NotBlank(message = "任务完成状态不能为空！")
    @ApiModelProperty("任务是否完成")
    private String finishedStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("日报创建时间")
    private Date dailyCreateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("日报修改时间")
    private Date modifyTime;

    @NotEmpty(message = "日报类型不能为空！")
    @ApiModelProperty("日报类型")
    private String dailyType;

    @NotEmpty(message = "时段不能为空！")
    @ApiModelProperty("时段")
    private String timeSlot;

//    @NotEmpty(message = "客户不能为空！")
    @ApiModelProperty("客户")
    private String customer;

//    @NotEmpty(message = "客户部门不能为空！")
    @ApiModelProperty("客户部门")
    private String dept;

//    @NotEmpty(message = "对接人不能为空！")
    @ApiModelProperty("对接人")
    private String contacts;


    /**
     * 0-草稿
     * 1-通过审批
     * 2-审批中
     * 3-驳回
     * 4-待确认 认领后 状态 保存置为3 提交置为2
     */
    @Field
    private Integer approveState ; // 审批状态


    private String approveUserId ; //审批人id

    private String approveUserName ; //审批人用户名

    private Float workCostTime ; // 工时

    private  String taskName ; //任务名称


    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String resultFileName ;
    @Field(type = FieldType.String, index= FieldIndex.not_analyzed)
    private String taskResultFileId ;

    //日报的参数 如 实习生的都是10
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
