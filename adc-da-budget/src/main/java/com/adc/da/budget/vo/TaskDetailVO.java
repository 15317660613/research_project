package com.adc.da.budget.vo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class TaskDetailVO {

    private String taskName;

    private String type;

    private String[] memberNames;

    private String[] memberIds;

    private String status;

    private String priority;

    private Date planStartTime;

    private Date planEndTime;

    private List<TaskStatusVO> taskStatusFinished;

    /**
     * 负责人id
     */
    private String approveUserId;

    /**
     * 负责人姓名
     */
    private String approveUserName;

    private Integer taskType = 0 ; //0是普通任务,1是商务任务

    private String pm;

    //业务管理员
    private String businessAdminId;

    private String businessAdminName;

    //项目管理员
    private String projectAdminId;

    private String projectLeaderId;

    private String projectLeader;

    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String projectAdminName;

}
