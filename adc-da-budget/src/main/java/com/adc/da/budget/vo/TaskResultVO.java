package com.adc.da.budget.vo;

import com.adc.da.budget.entity.TaskResultEO;
import lombok.Data;

import java.util.Date;

/**
 * <b>功能：</b>PF_TASK_RESULT TaskResultEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-09-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class TaskResultVO extends TaskResultEO {

    private String fileId ;
    private String fileName;
    private String fileSize ;
    private String fileType ;
    private String uploadUserName ;
    private String uploadUserId ;
    private Date uploadTime ;

}
