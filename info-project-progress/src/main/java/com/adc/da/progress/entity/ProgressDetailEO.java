package com.adc.da.progress.entity;

import lombok.Data;

import java.util.List;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-07-18
 */
@Data
public class ProgressDetailEO {

    /**
     * 流程回显名称，非流程定义名称
     */
    private String processName;

    /**
     * 流程实例
     */
    private List<InstanceDetailEO> processInstance;

    /**
     * 流程实例Key
     */
    private String processDefinitionKey;

    /**
     * 发起次数限制字段 ，
     * 默认为null，
     * 0为非流程回显已完成
     */
    private Integer processLimit;
}
