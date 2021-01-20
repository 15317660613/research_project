package com.adc.da.budget.query.budget;

import com.adc.da.base.page.BasePage;
import com.adc.da.budget.query.QueryVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * <b>功能：</b>TS_BUDGET BudgetEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-6-21 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class BudgetQuery extends BasePage {

    private String id;
    private String idOperator = "=";
    private List<String> deptIds;

    private Set<String> businessIds;

    private List<QueryVO> allObject = new ArrayList<>();

    private List<QueryVO> projectNames; //业务名称

    private List<QueryVO> propertys; //业务属性

    private List<QueryVO> propertyIds; //业务性质（类型）

    private List<QueryVO> usnames; //业务经理

    private List<QueryVO> cycleBegins; //周期开始

    private List<QueryVO> cycleEnds; //周期结束

    private List<QueryVO> finishedStatus; //业务状态

    private List<QueryVO> worktimes; //业务累计人天投入

    private List<QueryVO> createUserNames; //创建人

    private List<QueryVO> createTimes; //创建时间

    private List<QueryVO> updateTimes; //修改时间

}