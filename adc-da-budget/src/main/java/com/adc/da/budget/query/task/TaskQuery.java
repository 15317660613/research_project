package com.adc.da.budget.query.task;

import com.adc.da.budget.query.BaseESQueryPage;
import com.adc.da.budget.query.QueryVO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


/**
 * <b>功能：</b>TS_BUDGET BudgetEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-21 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Getter
@Setter
public class TaskQuery extends BaseESQueryPage {

    private List<QueryVO> names = new ArrayList<>(); //任务名称

    private List<QueryVO> prioritys = new ArrayList<>(); //优先级

    private List<QueryVO> types = new ArrayList<>(); //任务类型

    private List<QueryVO> memberNames = new ArrayList<>(); //任务成员

    private List<QueryVO> planStartTimes = new ArrayList<>(); //任务开始时间

    private List<QueryVO> planEndTimes = new ArrayList<>(); //任务结束时间

    private List<QueryVO> taskStatus = new ArrayList<>(); //任务状态

    private List<QueryVO> budgetNames = new ArrayList<>(); //任务所属业务

    private List<QueryVO> projectNames = new ArrayList<>(); //任务所属项目

    private List<QueryVO> workTimes = new ArrayList<>(); //任务累计人天投入

    private List<QueryVO> createUserNames = new ArrayList<>(); //创建人

    private List<QueryVO> createTimes = new ArrayList<>(); //创建时间

    private List<QueryVO> updateTimes = new ArrayList<>(); //修改时间

}
