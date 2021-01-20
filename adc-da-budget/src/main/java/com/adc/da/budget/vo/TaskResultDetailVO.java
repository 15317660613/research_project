package com.adc.da.budget.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TaskResultDetailVO {

    private String id ;

    private String taskId ;

    private String taskName ;

    private String taskTarget ;

    private List<String> memberNames ;

    private Date planStartTime ;

    private Date planEndTime ;

    private List<TaskResultVO> taskResultVOList ;

    private  String projectLeaderId ;

    private String pm ;

    private Date finishedTime;


}
