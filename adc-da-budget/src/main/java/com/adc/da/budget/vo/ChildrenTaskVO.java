package com.adc.da.budget.vo;

import com.adc.da.budget.entity.TaskResultEO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author : ZHAIKAIXUAN
 * @Date: 2018/11/22 09:50
 * @Description: 子任务
 */
@Document(indexName = "financial_prd", type = "childtask")
@Data
public class ChildrenTaskVO {

    @ApiModelProperty("子任务ID")
    private String id;

    @NotBlank(message = "子任务名称不能为空！")
    @ApiModelProperty("子任务名称")
    private String childTaskName;

//    @NotBlank(message = "所属项目不能为空！")
    @ApiModelProperty("项目ID")
    private String projectId;

    //@Field(type = FieldType.String, analyzer = "not_analyzed")
    @ApiModelProperty("项目负责人ID")
    private String projectLeaderId;

    @ApiModelProperty("项目负责人")
    private String projectLeader;

    @NotBlank(message = "所属任务不能为空！")
    @ApiModelProperty("所属任务ID")
    private String taskId;

    //@NotBlank(message = "所属人员不能为空！")
    @ApiModelProperty("所属人员ID")
    private String  personId;

    @ApiModelProperty("所属人员ID")
    private String  personName;

//    @NotBlank(message = "任务状态不能为空！")
    @ApiModelProperty("任务状态")
    private String status;

    @NotNull(message = "计划开始时间不能为空！")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty("计划开始时间")
    private Date planStartTime;

    @NotNull(message = "计划结束时间不能为空！")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty("计划结束时间")
    private Date planEndTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    @ApiModelProperty("子任务实际完成时间")
    private Date finishedActualTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    @ApiModelProperty("子任务创建时间")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    @ApiModelProperty("子任务修改时间")
    private Date modifyTime;

    private String createUserId;
    private String pm;
    private String businessCreateUserId;
    private Boolean manager;
    //判断改变状态按钮是否可用
    private String btnFlag;
    private String projectName;
    private String budgetId;
    private String budgetName;
    //工作流中下一节点的审批人
    private String approveUserId;
    private String approveUserName;

    private String taskTarget ;

    private List<TaskResultEO> taskResultEOList ;

    @ApiModelProperty("参与人员Id")
    private String[] memberIds;
    @ApiModelProperty("参与人员")
    private String projectMemberNames;

    private String[] memberNames;

    private List<Map<String,String>> mapsList;

    //项目成员更新时间
    @Field(type = FieldType.Date, analyzer = "not_analyzed")
    private Date projectMemberUpdatingTime;

    private List<Map<String,String>> userIdDeptNameMapList;
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
    private HashMap<String,List<String>> deptIdUserIdList = new HashMap<>();

////    {
////        "deptId":"[userId,userId]"
////    }
}
