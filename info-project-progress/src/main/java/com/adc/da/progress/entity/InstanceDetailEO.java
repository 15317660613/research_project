package com.adc.da.progress.entity;

import lombok.Data;

import java.util.Date;

import static com.adc.da.progress.util.StatusUtils.NOT_COMPLETED;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-07-18
 */
@Data
public class InstanceDetailEO {

    /**
     * 流程实例Id
     */
    private String processInstanceId;

    /**
     * 流程开始时间
     */
    private Date processStartTime;

    /**
     * 流程结束时间
     */
    private Date processEndTime;

    /**
     * 当前流程执行时间
     */
    private Date processTime;

    /**
     * 流程状态   未发起 已完成 等
     */
    private Integer processStatus = NOT_COMPLETED;

}
