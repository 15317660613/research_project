package com.adc.da.budget.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.List;
import java.util.Map;

/**
 * 人员日程响应DTO
 *
 * @author qichunxu
 */
@Data
@ToString
public class StaffScheduleResponseDTO implements Comparable<StaffScheduleResponseDTO>{
    @ApiModelProperty("日报ID")
    private String id;
    //所属项目ID)
    private String projectId;

    //业务名
    private String budgetId ;


    @ApiModelProperty("事件名称")
    private String eventName;

    @ApiModelProperty("工作描述")
    private String workDescription;

    @ApiModelProperty("处理的任务列表")
    private String[] taskIdArray;

    @ApiModelProperty("项目或者业务的名称列表")
    private String[] parentNames;

    @ApiModelProperty("任务处理状态")
    private String[] taskStatusArray;

    @ApiModelProperty("投入工时")
    private float[] workTimeArray;

    @ApiModelProperty("日报创建人ID")
    private String createUserId;

    @ApiModelProperty("任务是否完成")
    private String finishedStatus;

    @ApiModelProperty("日报创建时间")
    private String start;

    @ApiModelProperty("日报类型")
    private String dailyType;

    @ApiModelProperty("时段")
    private String timeSlot;

    @ApiModelProperty("客户")
    private String customer;

    @ApiModelProperty("客户部门")
    private String dept;

    @ApiModelProperty("对接人")
    private String contacts;

    private  Integer approveState ;


    /**
     * 0-草稿
     * 1-通过审批
     * 2-审批中
     * 3-驳回
     */

    private String approveUserId ; //审批人id

    private String approveUserName ; //审批人用户名

    private Float workCostTime ; // 工时


    private Float allFillWorkCostTime ;

    private Float approveFillWorkCostTime ;

    private  String taskName ; //任务名称
    //项目名
    private String projectName ;
    //业务名
    private String budgetName ;

    //成果

    private String resultFileName;
    private String taskResultFileId;

    private Boolean childTaskFlag; // 是子任务的传true  不是 传false  null 就是共享日报前的老数据

    private String dailyParentId;

    private String dailyParentCreateUserId;// 父日报创建人

    private String dailyParentCreateUserName; // 父日报创建人

    private String dailyComment; //日报备注

    private String[] childrenDailyCreateUserIds; // 子日报人接收人id

    private String[] childrenDailyCreateUserNames; // 子日报人接收人用户名

    private List<Map<String,String>> userIdDeptNameMapList; //防止悬浮显示部门时候用到

    @Override
    public int compareTo(StaffScheduleResponseDTO o) {
        if (null ==this.getTimeSlot() && null==o.getTimeSlot()){
            return 0;
        }
        if (null ==this.getTimeSlot()){
            return 1;
        }
        if (null==o.getTimeSlot()){
            return -1;
        }
      switch (this.getTimeSlot()){
          case "上午":
              if(o.getTimeSlot().equals("上午")){
                  return 0;
              }else if(o.getTimeSlot().equals("下午")){
                  return -1;
              }else{
                  return -1;
              }
          case "下午":
              if(o.getTimeSlot().equals("上午")){
                  return 1;
              }else if(o.getTimeSlot().equals("下午")){
                  return 0;
              }else{
                  return -1;
              }
          case "晚上":
              if(o.getTimeSlot().equals("上午")){
                  return 1;
              }else if(o.getTimeSlot().equals("下午")){
                  return 1;
              }else{
                  return 0;
              }
              default:
                  return 0;
      }
    }
}
