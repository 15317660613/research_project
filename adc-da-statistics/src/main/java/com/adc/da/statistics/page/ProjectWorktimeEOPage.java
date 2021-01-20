package com.adc.da.statistics.page;

import com.adc.da.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>功能：</b>ST_PROJECT_WORKTIME ProjectWorktimeEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Getter
@Setter
public class ProjectWorktimeEOPage extends BasePage {

    private String id;

    private List<String> ids ;

    private String idOperator = "=";

    private String projectId;

    private List<String> projectIds;

    private String projectIdOperator = "=";

    private String businessId;

    private String businessIdOperator = "=";

    private String dailyTime;

    private String dailyTime1;

    private String dailyTime2;

    private String dailyTimeOperator = "=";

    private String createTime;

    private String createTime1;

    private String createTime2;

    private String createTimeOperator = "=";

    private String worktime;

    private String worktimeOperator = "=";

    private String departmentId;

    private String departmentIdOperator = "=";

    private String extinfo1;

    private String extinfo1Operator = "=";

    private String extinfo2;

    private String extinfo2Operator = "=";

    private String extinfo3;

    private String extinfo3Operator = "=";

    private String extinfo4;

    private String extinfo4Operator = "=";

    private String extinfo5;

    private String extinfo5Operator = "=";

    private String extinfo6;

    private String extinfo6Operator = "=";

    private String year;

    private String yearOperator = "=";

    private String month;

    private String monthOperator = "=";

}
