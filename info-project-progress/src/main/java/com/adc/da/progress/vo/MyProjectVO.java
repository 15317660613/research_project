package com.adc.da.progress.vo;

import com.adc.da.progress.entity.ProjectBudgetChangeEO;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <b>功能：</b>PR_PROJECT_BUDGET_CHANGE ProjectBudgetChangeEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class MyProjectVO {

    private String projectLeaderId;

    private String projectLeaderName;

    private String bearDeptName;

    private String bearDeptId;

    private List<Map<String, String>> mapList;

    private String projectDescription;

    private Date projectBeginTime;

    private Date projectEndTime;

    private ProjectBudgetChangeEO projectBudgetChangeEO;

}
