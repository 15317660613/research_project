package com.adc.da.event.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName EventProcessVO
 * @Description: 用于包装返回简报流程信息
 * @Author 丁强
 * @Date 2019/11/4
 * @Version V1.0
 **/
@Data
public class EventProcessVO {
    private Integer status;
    private List<EventReceiveProcessVO> eventReceiveProcessVOList ;
}
