package com.adc.da.batchSyncData.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Document(indexName = "financial_prd", type = "Task")
public class TaskEO extends BaseEntity {
    private String id;
    private String projectId;
    private String projectLeaderId;

    private String budgetId;
    private String budgetName;
    private String budgetName1;

    private String deptId;
    private String projectName;
    private String projectName1;

    private String name;
    private String memberNameString;
    private String[] memberNames;
    //参与人员ID列表
    private String[] memberIds;
    private Date planStartTime;
    private Date planEndTime;
    private String priority;
    private String taskStatus;
    private Date finishedActualTime;

    private String createUserId;

    private String createUserName;

    private float workTime;    //工时

    private String pm;
    private String projectTeam;


    private List<Map<String,String>> mapsList;
    private Date createTime;

    private Date modifyTime;
    /**
     * 业务创建人字段
     */
    private String businessCreateUserId;

    //删除标记
    private Boolean delFlag;
    private Boolean manager;
    //前段判断是否改变状态按钮是否可用
    private String btnFlag;
    //任务目标
    private String taskTarget ;

    //任务类型  0-经营类任务   1-事务类任务   2-科研类任务
    private Integer projectType;

}