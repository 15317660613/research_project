package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.*;

@Document(indexName = "financial_prd", type = "Task")
@Data
@ToString
public class Task extends BaseEntity {

    @Id
    private String id;

    private String projectId;

    //@Field(type = FieldType.String, analyzer = "not_analyzed")
    private String projectLeaderId;

    //@Field(type = FieldType.String, analyzer = "not_analyzed")
    private String budgetId;

    @Excel(name = "业务名称", orderNum = "1")
    private String budgetName;

    private String budgetName1;

    /**
     * 用于搜索 业务信息
     */
    private String searchBudgetId;

    //@Field(type = FieldType.String, analyzer = "not_analyzed")
    //@Excel(name="部门ID")
    private String deptId;

    @Excel(name = "项目名称", orderNum = "2")
    private String projectName;

    private String projectName1;

    @Excel(name = "任务名称", orderNum = "3")
    private String name;

    @Excel(name = "参与成员", orderNum = "4")
    private String memberNameString;

    private String[] memberNames;

    //参与人员ID列表
    private String[] memberIds;

    @Excel(name = "任务计划开始时间", orderNum = "5", exportFormat = "yyyy-MM-dd HH:mm:ss", importFormat = "yyyy-MM-dd HH:mm:ss")
    private Date planStartTime;

    @Excel(name = "任务计划结束时间", orderNum = "6", exportFormat = "yyyy-MM-dd HH:mm:ss", importFormat = "yyyy-MM-dd HH:mm:ss")
    private Date planEndTime;

    @Excel(name = "优先级", orderNum = "7")
    private String priority;

    @Excel(name = "是否完成", orderNum = "8")
    private String taskStatus;

    @Excel(name = "任务实际完成时间", orderNum = "9", exportFormat = "yyyy-MM-dd HH:mm:ss", importFormat = "yyyy-MM-dd HH:mm:ss")
    private Date finishedActualTime;

    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String createUserId;

    /**
     * 该字段目前仅用于回显，不会往ES中注入数据
     * date 2019-10-18
     */
    private String createUserName;

    private float workTime;    //工时

    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String pm;

    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String projectTeam;

    private List<Map<String, String>> mapsList;

    private List<Map<String,String>> userIdDeptNameMapList;


    private Date createTime;


    private Date modifyTime;

    /**
     * 业务创建人字段
     */
    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String businessCreateUserId;

    //删除标记
    private Boolean delFlag;

    @ApiModelProperty("是否具有修改权限")
    private Boolean manager;

    //前段判断是否改变状态按钮是否可用
    private String btnFlag;

    //任务目标
    private String taskTarget;

    private List<TaskResultEO> taskResultEOList;

    // 0: 经营类项目任务  1:非经营项目任务  2: 日常事务类任务
    private Integer projectType;

    // 存储任务负责人  用于日报审批 编辑任务权限
    private String approveUserId;

    //仅用于回显
    private String approveUserName;

    private Integer taskType = 0 ; //0是普通任务,1是商务任务


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
    // 项目下所有任务的成员id
    @Field(ignoreFields = "childTaskMemberIdList")
    private List<String> childTaskMemberIdList;


}
