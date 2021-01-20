package com.adc.da.event.vo;

import lombok.Data;

/**
 * @ClassName EventReceiveProcessVO
 * @Description: TODO
 * @Author 丁强
 * @Date 2019/11/4
 * @Version V1.0
 **/
@Data
public class EventReceiveProcessVO {
    private String taskDefName;
    private String assignee;
    private String assigneeRealName;
    private String dealTime;
    private Integer status;
}
