package com.adc.da.business.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * add by liqiushi 20190315
 * 用于新首页统计各种维度的工时占比
 * 和项目的支出、创收统计排序
 */
@Data
public class StatisticsEntity extends BaseEntity implements Serializable {
    //没有对应ES分片，就当简单的javaBean使用
    private String userId;
    private String userName;
    private String projectId;
    private String projectName;
    private Double workTime;

    //机构信息 映射 TS_ORG 表
    private String orgId; //各级别机构同用id   这是 ID 回头问一下加什么注解改一下属性名
    private String Id;
    private String orgName;
    private String parentId;
    private String parentIds;
    private String delFlag;
    private String orgDesc;
    private String orgCode;
    private String orgType;
    private Integer isShow;
    private String remarks;

    //20190321 统计机构工时占比
    private String percent; // 百分比



    public StatisticsEntity() {
    }

    public StatisticsEntity(String Id, String orgName) {
        this.Id = Id;
        this.orgName = orgName;
    }

    public StatisticsEntity(Double workTime, String orgId, String orgName) {
        this.workTime = workTime;
        this.orgId = orgId;
        this.orgName = orgName;
    }
}
