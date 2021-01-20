package com.adc.da.budget.entity;

import com.adc.da.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/22 09:50
 * @Description: 子任务
 */
@Document(indexName = "financial_prd", type = "childtask")
@Data
@ToString
public class ChildrenTask {

    //子任务ID
    @Excel(name = "子任务ID",orderNum = "1")
    @Id
    private String id;
    //子任务名称
    @Excel(name = "子任务名称",orderNum = "2")
    private String childTaskName;
    /**
     * 项目ID
     */
    //@Excel(name = "项目ID",orderNum = "3")
    private String projectId;
    //@Field(type = FieldType.String, analyzer = "not_analyzed")
    private String projectLeaderId;
    //@Field(type = FieldType.String, analyzer = "not_analyzed")
    private String deptId;
    //所属任务ID
    @Excel(name = "所属任务ID",orderNum = "3")
    private String belongTaskName;
    private String taskId;
    //所属人员ID
    @Excel(name = "所属人员ID",orderNum = "4")
    private String personName;
    private String  personId;
    //计划开始时间
    @Excel(name = "计划开始时间",orderNum = "5",exportFormat = "yyyy-MM-dd HH:mm:ss", importFormat = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planStartTime;
    //计划结束时间
    @Excel(name = "计划结束时间",orderNum = "6",exportFormat = "yyyy-MM-dd HH:mm:ss", importFormat = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planEndTime;
    //任务状态
    @Excel(name = "任务状态",orderNum = "7")
    private String status;
    // 实际完成时间

    @Excel(name = "实际完成时间", orderNum = "8", exportFormat = "yyyy-MM-dd HH:mm:ss", importFormat = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date actualFinishedTime;
    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String createUserId;
    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String pm;
    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String projectTeam;
    private String budgetId;
    private String budgetName;

    private Date createTime;

    private Date modifyTime;

    /**
     * 业务创建人字段
     */
    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String businessCreateUserId;

    //删除标记
    private Boolean delFlag;

    private Boolean manager;
    //判断改变状态按钮是否可用
    private String btnFlag;
    private String projectName;
    //工作流中下一节点的审批人 仅回显 实际在 task中存储
    private String approveUserId;
    private String approveUserName;

    private String taskTarget ;

    private List<TaskResultEO> taskResultEOList ;

    private List<Map<String, String>> mapsList;

    private List<Map<String,String>> userIdDeptNameMapList;

    @ApiModelProperty("参与人员")
    private String[] memberNames;

    @NotEmpty(message = "参与成员不能为空！")
    @ApiModelProperty("参与人员Id")
    private String[] memberIds;

    @Excel(name = "参与成员", orderNum = "4")
    private String memberNameString;


    //业务管理员
    private String businessAdminId ;

    private String businessAdminName ;

    //项目管理员
    private String projectAdminId ;

    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String projectAdminName ;
    //任务成员更新时间
    @Field(type = FieldType.Date, analyzer = "not_analyzed")
    private Date projectMemberUpdatingTime;
    //    // 所选部门ID和类型 如类型=1 只选当前自身 类型=2 包含子部门
    private ArrayList<Map<String,String>> deptInfoListMap = new ArrayList<>();;
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
    private HashMap<String,List<String>> deptIdUserIdList= new HashMap<>();;
////    {
////        "deptId":"[userId,userId]"
////    }

}
