package com.adc.da.budget.vo;

import com.adc.da.budget.entity.TaskResultEO;
import com.adc.da.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

/**
 * @author : ZHAIKAIXUAN
 * @Date: 2018/11/19 17:46
 * @Description:
 */
@Data
public class TaskVO {

    @ApiModelProperty("任务ID")
    private String id;


    @NotBlank(message = "任务名称不能为空！")
    @ApiModelProperty("任务名称")
    private String name;

    @ApiModelProperty("业务ID")
    private String budgetId;
    private String budgetName;

    @ApiModelProperty("项目ID")
    private String projectId;

    //@Field(type = FieldType.String, analyzer = "not_analyzed")
    @ApiModelProperty("项目负责人ID")
    private String projectLeaderId;

    @ApiModelProperty("项目负责人")
    private String projectLeader;

    @ApiModelProperty("参与人员")
    private String projectMemberNames;
    private String[] memberNames;

    @NotEmpty(message = "参与成员不能为空！")
    @ApiModelProperty("参与人员Id")
    private String[] memberIds;

    @Excel(name = "参与成员", orderNum = "4")
    private String memberNameString;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("任务开始时间")
    private Date planStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("任务结束时间")
    private Date planEndTime;

    @NotBlank(message = "任务优先级不能为空！")
    @ApiModelProperty("任务优先级")
    private String priority;

    //@NotBlank(message = "完成状态不能为空！")
    @ApiModelProperty("是否完成")
    private String taskStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("任务实际完成时间")
    private Date finishedActualTime;
    @ApiModelProperty("创建人")
    private String createUserId;
    @ApiModelProperty("业务经理")
    private String pm;
    @ApiModelProperty("组")
    private String projectTeam;
    @ApiModelProperty("业务创建人")
    private String businessCreateUserId;
    @ApiModelProperty("是否具有修改权限")
    private Boolean manager;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("任务创建时间")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("任务修改时间")
    private Date modifyTime;

    private List<Map<String,String>> mapsList;

    private List<Map<String,String>> userIdDeptNameMapList;
    //前段判断是否改变状态按钮是否可用
    private String btnFlag;

    /**
     * 交付物
     */
    private String taskTarget ;

    private List<TaskResultEO> taskResultEOList ;

    /**
     * 任务负责人
     */
    private String approveUserId;

    private Integer taskType = 0 ; //0是普通任务,1是商务任务

    //项目成员更新时间
    private Date projectMemberUpdatingTime;


    //    // 所选部门ID和类型 如类型=1 只选当前自身 类型=2 包含子部门
    private ArrayList<Map<String,String>> deptInfoListMap = new ArrayList<>();
    ////    {
////        "deptId":"",
////        "type":1,
////        "deptName":""
////    },
////    {
////        "deptId":"",
////            "type":2,
////            "deptName":""
////    }
//    //所选部门id 及部门下用户id List
    private HashMap<String,List<String>> deptIdUserIdList= new HashMap<>();
////    {
////        "deptId":"[userId,userId]"
////    }
}
