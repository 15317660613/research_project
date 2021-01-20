package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;

@Data
public class UserProjectCustom extends BaseEntity {
    private String id;

    /**
     * 1业务2项目 3任务 4子任务
     */
    private String type;

    private String childtaskid;

    private String taskid;

    private String projectid;

    private String businessid;

    /**
     * 0已隐藏 1显示中
     */
    private String status;

    private String userid;
}