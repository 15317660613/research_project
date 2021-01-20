package com.adc.da.budget.vo;

import lombok.Data;

@Data
public class UserProjectWorkTimeVO {
    private String projectId ; //项目id
    private String projectName ;//项目名称
    private String userId ;//用户id
    private String userName ; //用户名
    private String workLevel ; //用户职级//职务
    private Float workTime ; //投入工时

}
